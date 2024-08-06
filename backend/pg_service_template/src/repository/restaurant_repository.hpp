#pragma once

#include <models/restaurant.hpp>
#include <models/restaurant_filter.hpp>
#include <userver/storages/postgres/cluster.hpp>
#include <userver/storages/postgres/io/row_types.hpp>
#include <vector>
#include <string>

#include "IRepositoryWithFilter.hpp"

namespace service {

class RestaurantRepository : IRepositoryWithFilter<TRestaurant, TRestaurantFilter> {
    userver::storages::postgres::ClusterPtr pg_cluster_;
    const std::string kTableName;

public:
    RestaurantRepository(const userver::storages::postgres::ClusterPtr& pg_cluster);

    std::vector<TRestaurant> GetAll() override;
    TRestaurant GetById(const std::string& id) override;
    std::vector<TRestaurant> GetByFilter(const TRestaurantFilter& filter) override;
};

} // service
