#include "selections.hpp"
#include <models/TSelection.hpp>
#include <lib/error_response_builder.hpp>

#include <fmt/format.h>

#include <userver/components/component.hpp>
#include <userver/formats/json/serialize.hpp>
#include <userver/formats/json/value.hpp>
#include <userver/logging/log.hpp>
#include <userver/server/handlers/http_handler_base.hpp>
#include <userver/server/http/http_request.hpp>
#include <userver/server/http/http_status.hpp>
#include <userver/storages/postgres/cluster.hpp>
#include <userver/storages/postgres/component.hpp>
#include <userver/utils/assert.hpp>

#include <service/SelectionService.hpp>
#include <boost/uuid/string_generator.hpp>
#include "lib/error_description.hpp"


namespace service {

namespace {

class SelectionController final : public userver::server::handlers::HttpHandlerBase {
 public:
  static constexpr std::string_view kName = "handler-selections";

    SelectionController(
        const userver::components::ComponentConfig& config,
        const userver::components::ComponentContext& component_context
    ) : 
    
    HttpHandlerBase(
        config,
        component_context
    ),
    selection_service_(
        component_context
        .FindComponent<SelectionService>()
    ),
    session_service_(
        component_context
        .FindComponent<SessionService>()
    )
    {}

    std::string HandleRequestThrow(
        const userver::server::http::HttpRequest& request,
        userver::server::request::RequestContext&
    ) const override 
    {
      request.GetHttpResponse().SetHeader( std::string_view("Access-Control-Allow-Origin"), "*" );
      request.GetHttpResponse().SetHeader( std::string_view("Access-Control-Allow-Headers"), "Content-Type, Authorization, Origin, X-Requested-With, Accept" );
      request.GetHttpResponse().SetHeader( std::string_view("Access-Control-Allow-Credentials"), "true" );

      if ( request.GetMethod() == userver::server::http::HttpMethod::kOptions ) {
            request.GetHttpResponse().SetHeader( std::string_view("Access-Control-Allow-Methods"),
                                                 "GET,HEAD,POST,PUT,DELETE,CONNECT,OPTIONS,PATCH" );
            request.GetHttpResponse().SetStatus( userver::server::http::HttpStatus::kOk );
            return "";
      }

      ErrorResponseBuilder errorBuilder(request);

      if (!request.HasHeader("Authorization")) {
        return errorBuilder.build(
            userver::server::http::HttpStatus::kUnauthorized,
            ErrorDescriprion::kTokenNotSpecified
        );
      }

      const auto& request_body_string = request.RequestBody();
      userver::formats::json::Value request_body_json = userver::formats::json::FromString(request_body_string);
      if (!request_body_json.HasMember("return_collections")) {
        return errorBuilder.build(
              userver::server::http::HttpStatus::kBadRequest,
              ErrorDescriprion::kReturnCollectionsNotSpecified
        );
      }

        boost::uuids::string_generator gen;
        auto user_id = session_service_.GetUserId(gen(request.GetCookie("session_id")));

        LOG_ERROR()<<boost::uuids::to_string(user_id);

      auto selections = selection_service_.GetAll(user_id, request_body_json["return_collections"].As<bool>());
        userver::formats::json::ValueBuilder responseJSON;
        responseJSON["items"].Resize(0);
        for (auto& selection : selections) {
            responseJSON["items"].PushBack(userver::formats::json::ValueBuilder{selection});
        }
      
        return userver::formats::json::ToPrettyString(
            responseJSON.ExtractValue(),
            {' ', 4}
        );
    }

    
    SelectionService& selection_service_;
    SessionService& session_service_;
    };
    
}   // namespace 



void AppendSelections(userver::components::ComponentList& component_list) {
  component_list.Append<SelectionController>();
}

}  // namespace service
