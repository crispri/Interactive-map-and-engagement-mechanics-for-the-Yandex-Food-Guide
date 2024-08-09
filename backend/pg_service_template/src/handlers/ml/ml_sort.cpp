#include "ml_sort.hpp"
#include "../../models/coordinates.hpp"
#include "../../lib/error_response_builder.hpp"
#include "../../models/restaurant.hpp"

#include <fmt/format.h>


#include <algorithm>
#include <userver/components/component.hpp>
#include <userver/formats/common/type.hpp>
#include <userver/formats/json/serialize.hpp>
#include <userver/formats/json/value.hpp>
#include <userver/logging/log.hpp>
#include <userver/server/handlers/http_handler_base.hpp>
#include <userver/storages/postgres/cluster.hpp>
#include <userver/storages/postgres/component.hpp>
#include <userver/utils/assert.hpp>

namespace service {

namespace {

class MLSort final : public userver::server::handlers::HttpHandlerBase {
 public:
  static constexpr std::string_view kName = "handler-ml_sort";

    MLSort(
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

        LOG_DEBUG() << "ABOBA";

        if (!request.HasHeader("Authorization")) {
          LOG_DEBUG() << "1!";
          LOG_INFO() << "1!";
          return errorBuilder.build(
          userver::server::http::HttpStatus::kUnauthorized,
          ErrorDescriprion::kTokenNotSpecified
          );
        }

        /*
        - Проверка авторизации пользователя.
        */


        const auto& request_body_string = request.RequestBody();
        userver::formats::json::Value request_body_json = userver::formats::json::FromString(request_body_string);

        if (!request_body_json.HasMember("restaurant_ids") || !request_body_json["restaurant_ids"].IsArray()) {
          LOG_DEBUG() << "2!";
          LOG_INFO() << "2!";

          return errorBuilder.build(
          userver::server::http::HttpStatus::kBadRequest,
          ErrorDescriprion::kListNotSpecified);
        }

        LOG_DEBUG() << "3!";
        LOG_INFO() << "3!";


        auto restaurant_ids = request_body_json["restaurant_ids"].As<std::vector<int>>();

        std::sort(restaurant_ids.begin(), restaurant_ids.end());


        for (auto i : restaurant_ids) LOG_DEBUG() << i, LOG_INFO() << i;

        userver::formats::json::ValueBuilder responseJSON;

        responseJSON["restaurant_ids"].Resize(0);

        for (int i = 0; i < (int)restaurant_ids.size(); ++i) {
            responseJSON["restaurant_ids"].PushBack(userver::formats::json::ValueBuilder{restaurant_ids[i]});
        }
        return userver::formats::json::ToPrettyString(responseJSON.ExtractValue(),
                                                    {' ', 4});

    }

  userver::storages::postgres::ClusterPtr pg_cluster_;
};

}  // namespace

void AppendMLSort(userver::components::ComponentList& component_list) {
  component_list.Append<MLSort>();
}

}  // namespace service
