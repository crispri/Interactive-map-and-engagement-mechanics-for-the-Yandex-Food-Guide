#include "restaurants.hpp"
#include <lib/error_response_builder.hpp>
#include <memory>
#include <models/TCoordinates.hpp>
#include <models/TRestaurant.hpp>

#include <unordered_map>
#include <fmt/format.h>

#include <string>
#include <string_view>
#include <userver/components/component.hpp>
#include <userver/formats/json/serialize.hpp>
#include <userver/formats/json/value.hpp>
#include <userver/logging/log.hpp>
#include <userver/server/handlers/http_handler_base.hpp>
#include <userver/server/http/http_status.hpp>
#include <userver/utils/assert.hpp>

#include <boost/uuid/string_generator.hpp>

#include <service/RestaurantService.hpp>

#include <models/RestaurantFilterJSON/IRestaurantFilterJSON.hpp>
#include <models/RestaurantFilterJSON/RatingRestaurantFilterJSON.hpp>
#include <models/RestaurantFilterJSON/PriceLBRestaurantFilterJSON.hpp>
#include <models/RestaurantFilterJSON/PriceUBRestaurantFilterJSON.hpp>
#include <models/RestaurantFilterJSON/OpenTimeRestaurantFilterJSON.hpp>
#include <models/RestaurantFilterJSON/CloseTimeRestaurantFilterJSON.hpp>
#include <models/RestaurantFilterJSON/SelectionRestaurantFilterJSON.hpp>
#include <models/RestaurantFilterJSON/TagRestaurantFilterJSON.hpp>
#include <userver/clients/http/client.hpp>
#include <userver/clients/http/component.hpp>
#include <boost/uuid/uuid_io.hpp>

namespace service {

    namespace {

        class RestaurantController final : public userver::server::handlers::HttpHandlerBase {
        public:
            static constexpr std::string_view kName = "handler-restaurants";

            RestaurantController(
                    const userver::components::ComponentConfig &config,
                    const userver::components::ComponentContext &component_context
            ) :

                    HttpHandlerBase(
                            config,
                            component_context
                    ),
                    restaurant_service_(
                            component_context
                                    .FindComponent<RestaurantService>()
                    ),
                    http_client_(
                            component_context
                                    .FindComponent<userver::components::HttpClient>().GetHttpClient()
                    ) {}

            std::string HandleRequestThrow(
                    const userver::server::http::HttpRequest &request,
                    userver::server::request::RequestContext &
            ) const override {
                request.GetHttpResponse().SetHeader(std::string_view("Access-Control-Allow-Origin"), "*");
                request.GetHttpResponse().SetHeader(std::string_view("Access-Control-Allow-Headers"),
                                                    "Content-Type, Authorization, Origin, X-Requested-With, Accept");
                request.GetHttpResponse().SetHeader(std::string_view("Access-Control-Allow-Credentials"), "true");

                if (request.GetMethod() == userver::server::http::HttpMethod::kOptions) {
                    request.GetHttpResponse().SetHeader(std::string_view("Access-Control-Allow-Methods"),
                                                        "GET,HEAD,POST,PUT,DELETE,CONNECT,OPTIONS,PATCH");
                    request.GetHttpResponse().SetStatus(userver::server::http::HttpStatus::kOk);
                    return "";
                }
                ErrorResponseBuilder errorBuilder(request);

        if (!request.HasHeader("Authorization")) {
            return errorBuilder.build(
                    userver::server::http::HttpStatus::kUnauthorized,
                    ErrorDescriprion::kTokenNotSpecified
            );
        }
        
        const auto &request_body_string = request.RequestBody();
        userver::formats::json::Value request_body_json = userver::formats::json::FromString(request_body_string);

                if (!request_body_json.HasMember("lower_left_corner") &&
                    !request_body_json.HasMember("top_right_corner")) {
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

        userver::storages::postgres::ParameterStore filter_params;
        std::string filter_string;
        bool only_collections = false;
        if (request_body_json.HasMember("only_collections")) {
            only_collections = request_body_json["only_collections"].As<bool>();
        }

                if (request_body_json.HasMember("filters")) {
                    if (!request_body_json["filters"].IsArray()) {
                        return errorBuilder.build(
                                userver::server::http::HttpStatus::kBadRequest,
                                ErrorDescriprion::kFiltersIsNotArray
                        );
                    }

                    std::size_t sz = request_body_json["filters"].GetSize();

                    for (std::size_t i = 0; i < sz; ++i) {
                        const auto &filter = request_body_json["filters"][i];

                        if (!filter.HasMember("property")) {
                            return errorBuilder.build(
                                    userver::server::http::HttpStatus::kBadRequest,
                                    ErrorDescriprion::kPropertyNotSpecified
                            );
                        }

                        if (!filter.HasMember("operator")) {
                            return errorBuilder.build(
                                    userver::server::http::HttpStatus::kBadRequest,
                                    ErrorDescriprion::kOperatorNotSpecified
                            );
                        }

                        if (!filter.HasMember("value")) {
                            return errorBuilder.build(
                                    userver::server::http::HttpStatus::kBadRequest,
                                    ErrorDescriprion::kValueNotSpecified
                            );
                        }

                        if (!filter["value"].IsArray()) {
                            return errorBuilder.build(
                                    userver::server::http::HttpStatus::kBadRequest,
                                    ErrorDescriprion::kValueIsNotArray
                            );
                        }

                        const auto &property_name = filter["property"].As<std::string>();

                        if (!StringRestaurantFilterMapping_.count(property_name)) {
                            LOG_ERROR() << property_name;
                            return errorBuilder.build(
                                    userver::server::http::HttpStatus::kBadRequest,
                                    ErrorDescriprion::kInvalidPropertyName
                            );
                        }

                        const auto &result =
                                StringRestaurantFilterMapping_
                                        .at(property_name)
                                        ->BuildSQLFilter(
                                                filter_params,
                                                filter
                                        );
                        if (std::holds_alternative<ErrorDescriprion>(result)) {
                            return errorBuilder.build(
                                    userver::server::http::HttpStatus::kBadRequest,
                                    std::get<ErrorDescriprion>(result)
                            );
                        }
                        filter_string += std::get<std::string>(result);
                        filter_string += " AND ";
                    }
                }

                LOG_ERROR() << "FILTER STRING = ." << filter_string << ".";

        TRestaurantFilter filters(
            request_body_json["lower_left_corner"].As<TCoordinates>(),
            request_body_json["top_right_corner"].As<TCoordinates>(),
            filter_params,
            filter_string,
            only_collections
        );

                boost::uuids::string_generator gen;
                auto user_id = gen("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");

                auto restaurants = restaurant_service_.GetByFilter(filters, user_id);


                userver::formats::json::ValueBuilder MLrequestJSON;
                MLrequestJSON["restaurant_ids"].Resize(0);
                for (auto &restaurant: restaurants) {
                    MLrequestJSON["restaurant_ids"].PushBack(
                            userver::formats::json::ValueBuilder{boost::uuids::to_string(restaurant.id)});

                    LOG_ERROR() << restaurant.id;
                }

                auto MLrequestString = userver::formats::json::ToPrettyString(MLrequestJSON.ExtractValue(),
                                                                              {' ', 4});


                http_client_.ResetUserAgent(boost::uuids::to_string(user_id));

                const auto MLresponse = http_client_.CreateRequest()
                        .post("http://localhost:8080/guide/v1/ml_rate", std::move(MLrequestString))
                        .timeout(std::chrono::seconds(1))
                        .retry(10)
                        .headers({ std::make_pair("Authorization", request.GetHeader("Authorization")) })
                        .perform();


                const auto MLresponseBody = MLresponse->body_view();

                userver::formats::json::Value MLresponseBodyJSON = userver::formats::json::FromString(
                        MLresponseBody);

                std::map<boost::uuids::uuid, int32_t> restaurant_scores;

                auto scores_field = MLresponseBodyJSON["scores"]
                        .As<std::vector<userver::formats::json::Value>>();


                for (auto &pairJSON : scores_field) {
                    auto restaurant_id = pairJSON["restaurant_id"].As<std::string>();
                    auto score = pairJSON["score"].As<int32_t>();

                    restaurant_scores[gen(restaurant_id)] = score;
                }

                assert(restaurant_scores.size() == restaurants.size());
                for (auto &restaurant: restaurants) {
                        restaurant.score = restaurant_scores[restaurant.id];
                }
                std::sort(restaurants.rbegin(), restaurants.rend());

                userver::formats::json::ValueBuilder responseJSON;
                responseJSON["items"].Resize(0);
                for (auto &restaurant: restaurants) {
                    responseJSON["items"].PushBack(userver::formats::json::ValueBuilder{restaurant});
                }

                return userver::formats::json::ToPrettyString(
                        responseJSON.ExtractValue(),
                        {' ', 4}
                );
            }

            RestaurantService &restaurant_service_;
            userver::clients::http::Client &http_client_;
            static const std::unordered_map<
                    std::string, std::shared_ptr<IRestaurantFilterJSON>
            > StringRestaurantFilterMapping_;
        };
        const std::unordered_map<
                std::string, std::shared_ptr<IRestaurantFilterJSON>
        > RestaurantController::StringRestaurantFilterMapping_ = {
                {"rating",            std::make_shared<RatingRestaurantFilterJSON>()},
                {"price_lower_bound", std::make_shared<PriceLBRestaurantFilterJSON>()},
                {"price_upper_bound", std::make_shared<PriceUBRestaurantFilterJSON>()},
                {"open_time",         std::make_shared<OpenTimeRestaurantFilterJSON>()},
                {"close_time",        std::make_shared<CloseTimeRestaurantFilterJSON>()},
                {"selection_id",      std::make_shared<SelectionRestaurantFilterJSON>()},
                {"tags",              std::make_shared<TagRestaurantFilterJSON>()}
        };

    }  // namespace

    void AppendRestaurantController(userver::components::ComponentList &component_list) {
        component_list.Append<RestaurantController>();
    }

}  // namespace service