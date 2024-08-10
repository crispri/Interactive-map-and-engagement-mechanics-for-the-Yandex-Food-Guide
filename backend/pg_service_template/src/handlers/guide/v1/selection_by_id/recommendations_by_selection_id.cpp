#include "recommendations_by_selection_id.hpp"
#include <boost/uuid/string_generator.hpp>
#include <lib/error_response_builder.hpp>
#include <models/restaurant.hpp>

#include <fmt/format.h>

#include <userver/components/component.hpp>
#include <userver/formats/json/serialize.hpp>
#include <userver/formats/json/value.hpp>
#include <userver/logging/log.hpp>
#include <userver/server/handlers/http_handler_base.hpp>
#include <userver/storages/postgres/cluster.hpp>
#include <userver/storages/postgres/component.hpp>
#include <userver/utils/assert.hpp>
#include <regex>
#include <boost/uuid/string_generator.hpp>



#include "service/SelectionService.hpp"

namespace service {

namespace {

    bool IsUUID(const std::string& id) {
      const std::regex uuid_regex(
          "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$");
      return std::regex_match(id, uuid_regex);
  }

class RecommendationsBySelectionId final : public userver::server::handlers::HttpHandlerBase {
 public:
  static constexpr std::string_view kName = "handler-recs-by-selection-id";

    RecommendationsBySelectionId(
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
    )
    {}

    std::string HandleRequestThrow(
        const userver::server::http::HttpRequest& request,
        userver::server::request::RequestContext&
    ) const override 
    {
      request.GetHttpResponse().SetHeader( std::string_view("Access-Control-Allow-Origin"), "*" );
      request.GetHttpResponse().SetHeader( std::string_view("Access-Control-Allow-Headers"), "true" );
      request.GetHttpResponse().SetHeader( std::string_view("Access-Control-Allow-Credentials"), "Content-Type, Authorization, Origin, X-Requested-With, Accept" );

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
      const auto& id = request.GetPathArg("id");
      if(!IsUUID(id)){
         return errorBuilder.build(
            userver::server::http::HttpStatus::kBadRequest,
            ErrorDescriprion::kInvalidSelectionId
        );
      }

      

        boost::uuids::string_generator gen;
        auto restaurants = selection_service_.GetById(gen(id));
        userver::formats::json::ValueBuilder responseJSON;
        responseJSON["items"].Resize(0);
        for (auto& restaurant : restaurants) {
            responseJSON["items"].PushBack(userver::formats::json::ValueBuilder{restaurant});
        }
      
        return userver::formats::json::ToPrettyString(
            responseJSON.ExtractValue(),
            {' ', 4}
        );                                        

    }

   SelectionService& selection_service_;
};

}  // namespace

void AppendReccomendationsBySelectionId(userver::components::ComponentList& component_list) {
  component_list.Append<RecommendationsBySelectionId>();
}

}  // namespace service
