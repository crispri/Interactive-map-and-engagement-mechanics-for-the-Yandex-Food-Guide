#include "collection-create.hpp"
#include <boost/uuid/uuid_io.hpp>
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

#include <boost/uuid/string_generator.hpp>
#include <service/SelectionService.hpp>
#include "lib/error_description.hpp"

#include <service/SessionService.hpp>

namespace service {

namespace {

class CollectionCreateController final : public userver::server::handlers::HttpHandlerBase {
 public:
  static constexpr std::string_view kName = "handler-collection-create";

    CollectionCreateController(
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

        const auto& request_body_string = request.RequestBody();
        userver::formats::json::Value request_body_json = userver::formats::json::FromString(request_body_string);
        if (!request_body_json.HasMember("name")) {
            return errorBuilder.build(
                userver::server::http::HttpStatus::kBadRequest,
                ErrorDescriprion::kCollectionNameNotSpecified
            );
        }

        if (!request_body_json.HasMember("description")) {
            return errorBuilder.build(
                userver::server::http::HttpStatus::kBadRequest,
                ErrorDescriprion::kCollectionDescriptionNotSpecified
            );
        }

        boost::uuids::string_generator gen;
        const auto& session_id = (request.HasCookie("session_id") ? request.GetCookie("session_id") : request.GetHeader("Authorization"));
        const auto& user_id = session_service_.GetUserId(gen(session_id));
        const auto& name = request_body_json["name"].As<std::string>();
        const auto& description = request_body_json["description"].As<std::string>(); 

        const auto& selection_id = selection_service_.CreateCollection(user_id, name, description);
        userver::formats::json::ValueBuilder responseJSON;
        responseJSON["id"] = boost::uuids::to_string(selection_id);
        
        return userver::formats::json::ToPrettyString(
            responseJSON.ExtractValue(),
            {' ', 4}
        );
    }

    
    SelectionService& selection_service_;
    SessionService& session_service_;
    };
    
}   // namespace 



void AppendCollectionCreate(userver::components::ComponentList& component_list) {
  component_list.Append<CollectionCreateController>();
}

}  // namespace service
