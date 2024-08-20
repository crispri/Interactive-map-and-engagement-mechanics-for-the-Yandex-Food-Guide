#include "ml_sort.hpp"
#include <lib/error_response_builder.hpp>
#include <models/TCoordinates.hpp>
#include <models/TRestaurant.hpp>
#include <boost/uuid/uuid_io.hpp>

#include <fmt/format.h>

#include <algorithm>
#include <userver/components/component.hpp>
#include <userver/formats/common/type.hpp>
#include <userver/formats/parse/common_containers.hpp>
#include <userver/formats/serialize/common_containers.hpp>
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

                boost::uuids::string_generator gen;
                // const auto& session_id = gen(request.HasCookie("session_id") ? request.GetCookie("session_id") : request.GetHeader("Authorization"));
                const auto& session_id = gen("5142cece-b22e-4a4f-adf9-990949d053ff");
                /*
                - Проверка авторизации пользователя.
                */
                LOG_ERROR() << "HERE mlrate0";
                const auto &request_body_string = request.RequestBody();
                LOG_ERROR() << "HERE mlrate2";
                userver::formats::json::Value request_body_json =
                        userver::formats::json::FromString(request_body_string);
                LOG_ERROR() << "HERE mlrate3";
                if (!request_body_json.HasMember("restaurant_ids") ||
                    !request_body_json["restaurant_ids"].IsArray()) 
                    {
                        LOG_ERROR() << "HERE mlrate4";
                    return errorBuilder.build(userver::server::http::HttpStatus::kBadRequest,
                                              ErrorDescriprion::kListNotSpecified);
                }
                LOG_ERROR() << "HERE mlrate5";
                auto user_id = ml_service_.GetUserIdByAuthToken(session_id);
                LOG_ERROR() << "HERE mlrate6";
                std::vector<boost::uuids::uuid> restaurant_ids;
                LOG_ERROR() << "HERE mlrate7";
                for (const auto& id : request_body_json["restaurant_ids"].As<std::vector<std::string>>()) {
                    LOG_ERROR() << "HERE mlrate8";
                    restaurant_ids.push_back(gen(id));
                }
                LOG_ERROR() << "HERE mlrate9";
                auto scores = service::MLService::SetScore(user_id, restaurant_ids);
                LOG_ERROR() << "HERE mlrate10";
                userver::formats::json::ValueBuilder responseJSON;
                LOG_ERROR() << "HERE mlrate11";

                responseJSON["scores"].Resize(0);
                LOG_ERROR() << "HERE mlrate12";
                for (auto &[restaurant_id, score]: scores) {
                    LOG_ERROR() << "HERE mlrate13";
                    userver::formats::json::ValueBuilder builder;
                    LOG_ERROR() << "HERE mlrate14";
                    builder["restaurant_id"] = boost::uuids::to_string(restaurant_id);
                    LOG_ERROR() << "HERE mlrate15";
                    builder["score"] = score;
                    LOG_ERROR() << "HERE mlrate16";
                    responseJSON["scores"].PushBack(builder.ExtractValue());
                    LOG_ERROR() << "HERE mlrate17";
                }
                LOG_ERROR() << "HERE mlrate18";
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
