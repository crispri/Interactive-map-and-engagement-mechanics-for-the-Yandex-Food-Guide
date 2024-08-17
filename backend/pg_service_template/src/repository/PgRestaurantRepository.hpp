#pragma once

#include <userver/storages/postgres/cluster.hpp>

#include "IRestaurantRepository.hpp"

namespace service {


class PgRestaurantRepository : public IRestaurantRepository {
    userver::storages::postgres::ClusterPtr pg_cluster_;
    const std::string kTableName_;

public:
    explicit PgRestaurantRepository(const userver::storages::postgres::ClusterPtr& pg_cluster);

    std::vector<TRestaurant> GetAll(const boost::uuids::uuid& user_id) override;
    std::optional<TRestaurant> GetById(const boost::uuids::uuid& restaurant_id, const boost::uuids::uuid& user_id) override;
    std::vector<TRestaurant> GetByFilter(const TRestaurantFilter& filter, const boost::uuids::uuid& user_id) override;
};

/*
 * в main кладем кладем конкретную реализацию.
 *
 */



} // service
