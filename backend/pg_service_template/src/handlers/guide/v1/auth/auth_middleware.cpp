#include "auth_middleware.hpp"
#include <boost/uuid/string_generator.hpp>
#include <userver/components/component_context.hpp>
#include <userver/formats/yaml/serialize.hpp>
#include "service/SessionService.hpp"

namespace service{

CheckSessionMiddleware::CheckSessionMiddleware
(const userver::server::handlers::HttpHandlerBase& handler,userver::yaml_config::YamlConfig middleware_config, SessionService& session_service)
:handler_(handler), middleware_config_(std::move(middleware_config)), session_service_(session_service){

}

void CheckSessionMiddleware::HandleRequest(userver::server::http::HttpRequest& request,
                                           userver::server::request::RequestContext& context) const {
    boost::uuids::string_generator gen;                                        
    auto session_cookie = request.GetCookie("session_id");

    if (request.GetRequestPath() != "/guide/v1/login" && (session_cookie.empty() || !session_service_.ValidateSession(gen(session_cookie)))) {
        request.SetResponseStatus(userver::server::http::HttpStatus::kUnauthorized);
        return;
    }
    Next(request, context);
}

std::unique_ptr<userver::server::middlewares::HttpMiddlewareBase> SomeHandlerMiddlewareFactory::Create(
    const userver::server::handlers::HttpHandlerBase& handler,
    userver::yaml_config::YamlConfig middleware_config) const {
    return std::make_unique<CheckSessionMiddleware>(handler, std::move(middleware_config), session_service_);
}
SomeHandlerMiddlewareFactory::SomeHandlerMiddlewareFactory(const userver::components::ComponentConfig& config,
                            const userver::components::ComponentContext& context) : userver::server::middlewares::HttpMiddlewareFactoryBase(config, context), context_(context), session_service_(context_.FindComponent<SessionService>()){
                                
                            }

// Реализация метода GetMiddlewareConfigSchema в SomeHandlerMiddlewareFactory
userver::yaml_config::Schema SomeHandlerMiddlewareFactory::GetMiddlewareConfigSchema() const {
    return userver::formats::yaml::FromString(R"(
type: object
description: Config for this particular middleware
additionalProperties: false
properties:
    header-value:
        type: string
        description: header value to set for responses
)")
    .As<userver::yaml_config::Schema>();
}


void AppendSessionController(userver::components::ComponentList& component_list) {
    component_list.Append<SomeHandlerMiddlewareFactory>();                
}

}