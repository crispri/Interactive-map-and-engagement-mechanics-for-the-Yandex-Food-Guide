#include "PgSessionRepository.hpp"

namespace service{
    SessionRepository::SessionRepository(userver::storages::postgres::ClusterPtr pg_cluster)
        : pg_cluster_(pg_cluster), kSessionsTableName_("guide.auth") {}

    std::optional<TSession> SessionRepository::FindBySessionId(const boost::uuids::uuid& session_id) {
        auto result = pg_cluster_->Execute(
            userver::storages::postgres::ClusterHostType::kSlave,
            R"(SELECT user_id, session_id, expiration_time FROM )" + kSessionsTableName_ + R"( WHERE session_id = $1;)",
            session_id);

        if (result.IsEmpty()) {
            return std::nullopt;
        }

        return result[0].As<TSession>(userver::storages::postgres::kRowTag);
    }

    void SessionRepository::UpdateSessionExpiration(const boost::uuids::uuid& session_id) {
        auto new_expiration_time = std::chrono::system_clock::now() + std::chrono::hours(1);
        pg_cluster_->Execute(
            userver::storages::postgres::ClusterHostType::kSlave,
            R"(UPDATE )" + kSessionsTableName_ + R"( SET expiration_time = $1 WHERE session_id = $2;)",
            new_expiration_time, session_id);
    }
}