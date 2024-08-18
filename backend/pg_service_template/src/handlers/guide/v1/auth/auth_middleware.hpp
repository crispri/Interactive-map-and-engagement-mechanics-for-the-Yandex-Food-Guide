#pragma once

#include <userver/server/handlers/http_handler_base.hpp>
#include <userver/server/middlewares/http_middleware_base.hpp>
#include <service/SessionService.hpp>
#include "repository/PgSessionRepository.hpp"
#include "service/SessionService.hpp"

namespace service {
    class CheckSessionMiddleware : public userver::server::middlewares::HttpMiddlewareBase {
    public:
        static constexpr std::string_view kName {"check-session-middleware"};

        
        CheckSessionMiddleware(const userver::server::handlers::HttpHandlerBase& handler,
                          userver::yaml_config::YamlConfig middleware_config, SessionService& session_service) ;

        void HandleRequest(userver::server::http::HttpRequest& request,
                         userver::server::request::RequestContext& context) const override;

    private:
        const userver::server::handlers::HttpHandlerBase& handler_;
        userver::yaml_config::YamlConfig middleware_config_;
        SessionService& session_service_;
        
    };


    class SomeHandlerMiddlewareFactory final : public userver::server::middlewares::HttpMiddlewareFactoryBase {
    public:
        static constexpr std::string_view kName{CheckSessionMiddleware::kName};

        SomeHandlerMiddlewareFactory(const userver::components::ComponentConfig&,
                            const userver::components::ComponentContext&);


        using HttpMiddlewareFactoryBase::HttpMiddlewareFactoryBase;

    private:
        std::unique_ptr<userver::server::middlewares::HttpMiddlewareBase> Create(
            const userver::server::handlers::HttpHandlerBase& handler,
            userver::yaml_config::YamlConfig middleware_config) const override;

        userver::yaml_config::Schema GetMiddlewareConfigSchema() const override;
        const userver::components::ComponentContext& context_;
        SessionService& session_service_;
        
};
void AppendSessionController(userver::components::ComponentList& component_list);

    
    
}
