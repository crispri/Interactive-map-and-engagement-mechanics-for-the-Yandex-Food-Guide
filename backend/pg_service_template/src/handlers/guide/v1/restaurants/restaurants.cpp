#include "restaurants.hpp"
#include <lib/error_response_builder.hpp>
#include <models/TCoordinates.hpp>
#include <models/TRestaurant.hpp>

#include <fmt/format.h>

#include <string>
#include <string_view>
#include <userver/components/component.hpp>
#include <userver/formats/json/serialize.hpp>
#include <userver/formats/json/value.hpp>
#include <userver/logging/log.hpp>
#include <userver/server/handlers/http_handler_base.hpp>
#include <userver/utils/assert.hpp>

#include <boost/uuid/string_generator.hpp>

#include <service/RestaurantService.hpp>

#include <models/RestaurantFilterJSON/IRestaurantFilterJSON.hpp>
#include <models/RestaurantFilterJSON/RatingRestaurantFilterJSON.hpp>
#include <models/RestaurantFilterJSON/PriceLBRestaurantFilterJSON.hpp>
#include <models/RestaurantFilterJSON/PriceUBRestaurantFilterJSON.hpp>
#include <models/RestaurantFilterJSON/OpenTimeRestaurantFilterJSON.hpp>
#include <models/RestaurantFilterJSON/CloseTimeRestaurantFilterJSON.hpp>

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
    restaurant_service_(
        component_context
        .FindComponent<RestaurantService>()
    )
    {}

    std::string HandleRequestThrow(
        const userver::server::http::HttpRequest& request,
        userver::server::request::RequestContext&
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



        userver::storages::postgres::ParameterStore filter_params;
        std::string filter_string;


        if (request_body_json.HasMember("filters")) {
            if (!request_body_json["filters"].IsArray()) {
                return errorBuilder.build(
                        userver::server::http::HttpStatus::kBadRequest,
                        ErrorDescriprion::kFiltersIsNotArray
                );
            }

            std::size_t sz = request_body_json["filters"].GetSize();

            for (std::size_t i = 0; i < sz; ++i) {
                const auto& filter = request_body_json["filters"][i];

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

                const auto& property_name = filter["property"].As<std::string>();

                if (!StringRestaurantFilterMapping_.count(property_name)) {
                    LOG_ERROR() << property_name;
                    return errorBuilder.build(
                            userver::server::http::HttpStatus::kBadRequest,
                            ErrorDescriprion::kInvalidPropertyName
                    );
                }

                const auto& result =
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
            filter_string
        );

        auto restaurants = restaurant_service_.GetByFilter(filters);
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

    RestaurantService& restaurant_service_;
    static const std::unordered_map<
        std::string, std::shared_ptr<IRestaurantFilterJSON>
    > StringRestaurantFilterMapping_;
};

const std::unordered_map<
        std::string, std::shared_ptr<IRestaurantFilterJSON>
> RestaurantController::StringRestaurantFilterMapping_ = {
    {"rating", std::make_shared<RatingRestaurantFilterJSON>()},
    {"price_lower_bound", std::make_shared<PriceLBRestaurantFilterJSON>()},
    {"price_upper_bound", std::make_shared<PriceUBRestaurantFilterJSON>()},
    {"open_time", std::make_shared<OpenTimeRestaurantFilterJSON>()},
    {"close_time", std::make_shared<CloseTimeRestaurantFilterJSON>()}
};

}  // namespace

void AppendRestaurantController(userver::components::ComponentList& component_list) {
    component_list.Append<RestaurantController>();
}

}  // namespace service
