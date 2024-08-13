#include "ml_sort.hpp"
#include <lib/error_response_builder.hpp>
#include <models/TCoordinates.hpp>
#include <models/TRestaurant.hpp>
#include <boost/uuid/uuid_io.hpp>

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

#include <boost/uuid/string_generator.hpp>

#include <service/MLService.hpp>
#include <boost/lexical_cast.hpp>

namespace service {

    namespace {

        class MLSort final : public userver::server::handlers::HttpHandlerBase {
        public:
            static constexpr std::string_view kName = "handler-ml_rate";

            MLSort(const userver::components::ComponentConfig &config,
                   const userver::components::ComponentContext &component_context)
                    :

                    HttpHandlerBase(config, component_context),
                    ml_service_(
                            component_context
                                    .FindComponent<MLService>()
                    ) {}

            std::string HandleRequestThrow(
                    const userver::server::http::HttpRequest &request,
                    userver::server::request::RequestContext &) const override {
                ErrorResponseBuilder errorBuilder(request);

                if (!request.HasHeader("Authorization")) {
                    return errorBuilder.build(
                            userver::server::http::HttpStatus::kUnauthorized,
                            ErrorDescriprion::kTokenNotSpecified);
                }

                boost::uuids::string_generator gen;
                auto session_id = gen(request.GetHeader("Authorization"));

                /*
                - Проверка авторизации пользователя.
                */

                const auto &request_body_string = request.RequestBody();
                userver::formats::json::Value request_body_json =
                        userver::formats::json::FromString(request_body_string);

                if (!request_body_json.HasMember("restaurant_ids") ||
                    !request_body_json["restaurant_ids"].IsArray()) {
                    return errorBuilder.build(userver::server::http::HttpStatus::kBadRequest,
                                              ErrorDescriprion::kListNotSpecified);
                }

                auto user_id = ml_service_.GetUserIdByAuthToken(session_id);

                auto restaurant_ids =
                        request_body_json["restaurant_ids"].As<std::vector<int>>();

                auto scores = service::MLService::SetScore(user_id, restaurant_ids);


                userver::formats::json::ValueBuilder responseJSON;

                responseJSON["scores"].Resize(0);

                for (auto &[restaurant_id, score]: scores) {
                    userver::formats::json::ValueBuilder builder;
                    builder["restaurant_id"] = restaurant_id;
                    builder["score"] = score;
                    responseJSON["scores"].PushBack(builder.ExtractValue());
                }
                return userver::formats::json::ToPrettyString(responseJSON.ExtractValue(),
                                                              {' ', 4});
            }

            MLService &ml_service_;
        };

    }  // namespace

    void AppendMLRate(userver::components::ComponentList &component_list) {
        component_list.Append<MLSort>();
    }

}  // namespace service
