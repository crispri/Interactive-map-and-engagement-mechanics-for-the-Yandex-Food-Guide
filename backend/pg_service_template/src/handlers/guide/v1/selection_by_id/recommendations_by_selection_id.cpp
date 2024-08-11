#include "recommendations_by_selection_id.hpp"
#include "../../../../lib/error_response_builder.hpp"
#include "../../../../models/TRestaurant.hpp"

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
    pg_cluster_(
      component_context
        .FindComponent<userver::components::Postgres>("postgres-db-1")
        .GetCluster()
    )
    {}

    std::string HandleRequestThrow(
        const userver::server::http::HttpRequest& request,
        userver::server::request::RequestContext&
    ) const override 
    {
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
            ErrorDescriprion::kWrongIdSelection
        );
      }

      

      TRestaurant restaurant;

      restaurant.address = "ул.Москворечье д.19";
      restaurant.coordinates = {3.45, 15.31};
      restaurant.description = "Азиатская кухня";
      restaurant.is_approved = false;
      restaurant.is_favorite = false;
      restaurant.name = "Тануки";
      restaurant.price_lower_bound = 500;
      restaurant.price_upper_bound = 1500;
      restaurant.rating = 4.10;
    

      userver::formats::json::ValueBuilder responseJSON{restaurant};
      return userver::formats::json::ToPrettyString(responseJSON.ExtractValue(),
                                                  {' ', 4});

    }

  userver::storages::postgres::ClusterPtr pg_cluster_;
};

}  // namespace

void AppendReccomendationsBySelectionId(userver::components::ComponentList& component_list) {
  component_list.Append<RecommendationsBySelectionId>();
}

}  // namespace service
