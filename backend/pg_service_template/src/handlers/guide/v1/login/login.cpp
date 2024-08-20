#include "login.hpp"

#include <fmt/format.h>

#include <userver/components/component.hpp>
#include <userver/formats/json/serialize.hpp>
#include <userver/formats/json/value.hpp>
#include <userver/logging/log.hpp>
#include <userver/server/handlers/http_handler_base.hpp>
#include <userver/storages/postgres/cluster.hpp>
#include <userver/storages/postgres/component.hpp>
#include <boost/uuid/string_generator.hpp>

namespace service {

    namespace {

        class Login final : public userver::server::handlers::HttpHandlerBase {
        public:
            static constexpr std::string_view kName = "handler-login";

            Login(
                    const userver::components::ComponentConfig& config,
                    const userver::components::ComponentContext& component_context
            ) :

                    HttpHandlerBase(
                            config,
                            component_context
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

                request.GetHttpResponse().SetHeader(std::string_view("Set-Cookie"), "session_id=5142cece-b22e-4a4f-adf9-990949d053ff");
                return "";
            }
        };

    }  // namespace

    void AppendLogin(userver::components::ComponentList& component_list) {
        component_list.Append<Login>();
    }

}  // namespace service
