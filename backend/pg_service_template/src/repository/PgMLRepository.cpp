#include "PgMLRepository.hpp"

namespace service {


    PgMLRepository::PgMLRepository(const userver::storages::postgres::ClusterPtr &pg_cluster) :
            pg_cluster_(pg_cluster),
            kTableName_("guide.auth") {}

    boost::uuids::uuid PgMLRepository::GetUserIdByAuthToken(const boost::uuids::uuid &session_id) {
        const auto &user_id = pg_cluster_->Execute(
                userver::storages::postgres::ClusterHostType::kSlave,
                R"(SELECT user_id FROM )" + kTableName_ + " WHERE session_id = '" +
                boost::uuids::to_string(session_id) +"';"
        ).AsSingleRow<boost::uuids::uuid>();
        return user_id;
    }


} // service