#include "SessionService.hpp"
#include <boost/uuid/uuid.hpp>
#include <userver/components/component_context.hpp>
#include <userver/storages/postgres/component.hpp>

namespace service {

SessionService::SessionService(
    const userver::components::ComponentConfig &config,
    const userver::components::ComponentContext &context
) :
    userver::components::ComponentBase(
        config,
        context
    ),
    repository_(
        std::make_shared<SessionRepository>(
            context
            .FindComponent<userver::components::Postgres>("postgres-db-1")
            .GetCluster()
        )
    )
{}

bool SessionService::ValidateSession(const boost::uuids::uuid& session_id) {
    auto session = repository_->FindBySessionId(session_id);
    if (session && session->expiration_time > std::chrono::system_clock::now()) {
        repository_->UpdateSessionExpiration(session_id);
        return true;
    }
    return false;
}

boost::uuids::uuid SessionService::GetUserId(const boost::uuids::uuid& session_id) {
    auto session = repository_->FindBySessionId(session_id);
   
    return session->user_id;
}





void AppendSessionService(userver::components::ComponentList& component_list) {
    component_list.Append<SessionService>();
}

} // namespace service