#pragma once

#include <userver/storages/postgres/cluster.hpp>

#include "IMLRepository.hpp"

namespace service {


    class PgMLRepository : public IMLRepository {
        userver::storages::postgres::ClusterPtr pg_cluster_;
        const std::string kTableName_;

    public:
        explicit PgMLRepository(const userver::storages::postgres::ClusterPtr &pg_cluster);

    private:
        boost::uuids::uuid GetUserIdByAuthToken(const boost::uuids::uuid &) override;
    };

/*
 * в main кладем кладем конкретную реализацию.
 *
 */



} // service
