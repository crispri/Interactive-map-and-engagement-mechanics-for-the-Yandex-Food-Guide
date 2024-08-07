#include "restaurants.hpp"
#include <lib/error_response_builder.hpp>
#include <models/coordinates.hpp>
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

#include <boost/uuid/string_generator.hpp>

#include <service/RestaurantService.hpp>

namespace service {

namespace {

class RestaurantController final : public userver::server::handlers::HttpHandlerBase {
 public:
  static constexpr std::string_view kName = "handler-restaurants";

    RestaurantController(
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

      TRestaurantFilter filters(
          request_body_json["lower_left_corner"].As<TCoordinates>(),
          request_body_json["top_right_corner"].As<TCoordinates>()
      );

      RestaurantService service(pg_cluster_);

      auto restaurants = service.GetByFilter(filters);
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

    userver::storages::postgres::ClusterPtr pg_cluster_;
};

}  // namespace

void AppendRestaurantController(userver::components::ComponentList& component_list) {
  component_list.Append<RestaurantController>();
}

}  // namespace service
