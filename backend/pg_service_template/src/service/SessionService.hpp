#pragma once

#include <repository/PgSessionRepository.hpp>
#include <userver/components/component_list.hpp>
#include <userver/components/component_base.hpp>

namespace service{

    class SessionService final : public userver::components::ComponentBase {
    std::shared_ptr<SessionRepository> repository_;

    public:
    static constexpr std::string_view kName = "session-service-component";

    SessionService(
            const userver::components::ComponentConfig &config,
            const userver::components::ComponentContext &context
    );

    bool ValidateSession(const boost::uuids::uuid& session_id);
    boost::uuids::uuid GetUserId(const boost::uuids::uuid& session_id);

    };

    void AppendSessionService(
    userver::components::ComponentList& component_list
);

}