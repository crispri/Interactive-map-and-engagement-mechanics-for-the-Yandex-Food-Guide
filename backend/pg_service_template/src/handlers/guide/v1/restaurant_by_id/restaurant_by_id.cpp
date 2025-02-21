#include "restaurant_by_id.hpp"
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
#include <userver/server/http/http_status.hpp>
#include <userver/utils/assert.hpp>

#include <boost/uuid/string_generator.hpp>

#include <service/RestaurantService.hpp>
#include <boost/lexical_cast.hpp>
#include "lib/error_description.hpp"


#include <service/SessionService.hpp>


namespace service {

namespace {

class RestaurantByIdController final : public userver::server::handlers::HttpHandlerBase {
public:
    static constexpr std::string_view kName = "handler-restaurant-by-id";

    RestaurantByIdController(
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
    ),
    session_service_(
            component_context
                    .FindComponent<SessionService>()
    )
    {}

    std::string HandleRequestThrow(
        const userver::server::http::HttpRequest& request,
        userver::server::request::RequestContext&
    ) const override 
    {
        request.GetHttpResponse().SetHeader( std::string_view("Access-Control-Allow-Origin"), "*" );
        request.GetHttpResponse().SetHeader( std::string_view("Access-Control-Allow-Headers"), "Content-Type, Authorization, Origin, X-Requested-With, Accept" );
        request.GetHttpResponse().SetHeader( std::string_view("Access-Control-Allow-Credentials"), "true" );

        if ( request.GetMethod() == userver::server::http::HttpMethod::kOptions ) {
            request.GetHttpResponse().SetHeader( std::string_view("Access-Control-Allow-Methods"),
                                                 "GET,HEAD,POST,PUT,DELETE,CONNECT,OPTIONS,PATCH" );
            request.GetHttpResponse().SetStatus( userver::server::http::HttpStatus::kOk );
            return "";
        }
        
        ErrorResponseBuilder errorBuilder(request);

        boost::uuids::string_generator gen;
        auto restaurant_id = gen(request.GetPathArg("id"));
        // const auto& session_id = (request.HasCookie("session_id") ? request.GetCookie("session_id") : request.GetHeader("Authorization"));
        // const auto& user_id = session_service_.GetUserId(gen(session_id));
        const auto& user_id = gen("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");
        auto restaurant = restaurant_service_.GetById(restaurant_id, user_id);
        if (!restaurant) {
            return errorBuilder.build(
                userver::server::http::HttpStatus::kNotFound,
                ErrorDescriprion::kRestaurantNotFound
            );
        }
        return userver::formats::json::ToPrettyString(
            userver::formats::json::ValueBuilder{restaurant.value()}.ExtractValue(),
            {' ', 4}
        );
    }

    RestaurantService& restaurant_service_;
    SessionService& session_service_;
};

}  // namespace

void AppendRestaurantByIdController(userver::components::ComponentList& component_list) {
    component_list.Append<RestaurantByIdController>();                
}

}  // namespace service
