#include "ml_sort.hpp"
#include <lib/error_response_builder.hpp>
#include <models/TRestaurant.hpp>

#include <fmt/format.h>

#include <algorithm>
#include <userver/formats/parse/common_containers.hpp>
#include <userver/formats/serialize/common_containers.hpp>
#include <userver/components/component.hpp>
#include <userver/formats/common/type.hpp>
#include <userver/formats/json/serialize.hpp>
#include <userver/formats/json/value.hpp>
#include <userver/logging/log.hpp>
#include <userver/server/handlers/http_handler_base.hpp>
#include <userver/storages/postgres/cluster.hpp>
#include <userver/storages/postgres/component.hpp>
#include <userver/utils/assert.hpp>

#include <service/MLService.hpp>
#include <boost/uuid/string_generator.hpp>

namespace service {

    namespace {

        class MLSort final : public userver::server::handlers::HttpHandlerBase {
        public:
            static constexpr std::string_view kName = "handler-ml_sort";

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

                boost::uuids::string_generator gen;
                std::vector<boost::uuids::uuid> restaurant_ids;

                for (const auto &id: request_body_json["restaurant_ids"].As<std::vector<std::string>>()) {
                    restaurant_ids.push_back(gen(id));
                }


                service::MLService::MLSort(restaurant_ids);

                userver::formats::json::ValueBuilder responseJSON;

                responseJSON["restaurant_ids"].Resize(0);

                for (auto restaurant_id: restaurant_ids) {
                    responseJSON["restaurant_ids"].PushBack(
                            userver::formats::json::ValueBuilder{boost::uuids::to_string(restaurant_id)});
                }
                return userver::formats::json::ToPrettyString(responseJSON.ExtractValue(),
                                                              {' ', 4});
            }


            MLService &ml_service_;
        };

    }  // namespace

    void AppendMLSort(userver::components::ComponentList &component_list) {
        component_list.Append<MLSort>();
    }

}  // namespace service
