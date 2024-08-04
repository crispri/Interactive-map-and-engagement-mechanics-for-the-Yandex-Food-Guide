#include "recommendations.hpp"
#include "../../models/coordinates.hpp"
#include "../../lib/error_response_builder.hpp"
#include "../../models/restaurant.hpp"

#include <fmt/format.h>

#include <userver/components/component.hpp>
#include <userver/formats/json/serialize.hpp>
#include <userver/formats/json/value.hpp>
#include <userver/logging/log.hpp>
#include <userver/server/handlers/http_handler_base.hpp>
#include <userver/storages/postgres/cluster.hpp>
#include <userver/storages/postgres/component.hpp>
#include <userver/utils/assert.hpp>

namespace service {

namespace {

class Recommendations final : public userver::server::handlers::HttpHandlerBase {
 public:
  static constexpr std::string_view kName = "handler-recommendations";

    Recommendations(
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

      /*
        TODO:
          - Проверка авторизации пользователя.
      */
      
      const auto& request_body_string = request.RequestBody();
      userver::formats::json::Value request_body_json = userver::formats::json::FromString(request_body_string);


      if (!request_body_json.HasMember("lower_left_corner") && !request_body_json.HasMember("top_right_corner")) {
        return errorBuilder.build(
            userver::server::http::HttpStatus::kBadRequest,
            ErrorDescriprion::kCornersNotSpecified
        );
      }
      if (!request_body_json.HasMember("lower_left_corner")) {
        return errorBuilder.build(
            userver::server::http::HttpStatus::kBadRequest,
            ErrorDescriprion::kLowerLeftCornerNotSpecified
        );
      }
      if (!request_body_json.HasMember("top_right_corner")) {
        return errorBuilder.build(
            userver::server::http::HttpStatus::kBadRequest,
            ErrorDescriprion::kTopRightCornerNotSpecified
        );
      }

      // auto lower_left_corner = request_body_json["lower_left_corner"].As<TCoordinates>();
      // auto top_right_corner = request_body_json["top_right_corner"].As<TCoordinates>();
      
      TRestaurant restaurant;

      restaurant.address = "ул пушкина";
      restaurant.coordinates = {2.28, 13.37};
      restaurant.description = "описание";
      restaurant.is_approved = false;
      restaurant.is_favorite = false;
      restaurant.name = "мак";
      restaurant.price_lower_bound = 228;
      restaurant.price_upper_bound = 1337;
      restaurant.rating = 2.28;
      // restaurant.tags = {"aboba", "ameba"};

      userver::formats::json::ValueBuilder responseJSON{restaurant};
      return userver::formats::json::ToPrettyString(responseJSON.ExtractValue(),
                                                  {' ', 4});

    }

  userver::storages::postgres::ClusterPtr pg_cluster_;
};

}  // namespace

void AppendRecommendations(userver::components::ComponentList& component_list) {
  component_list.Append<Recommendations>();
}

}  // namespace service
