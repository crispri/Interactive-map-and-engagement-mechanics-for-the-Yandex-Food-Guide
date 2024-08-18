#pragma once
#include <userver/storages/postgres/cluster.hpp>
#include <userver/storages/postgres/result_set.hpp>
#include <optional>
#include <chrono>
#include <models/session.hpp>

namespace service{

    class SessionRepository {
    public:
        explicit SessionRepository(userver::storages::postgres::ClusterPtr pg_cluster);

        std::optional<TSession> FindBySessionId(const boost::uuids::uuid& session_id);
        void UpdateSessionExpiration(const boost::uuids::uuid& session_id);

    private:
        userver::storages::postgres::ClusterPtr pg_cluster_;
        const std::string kSessionsTableName_;
    };

}