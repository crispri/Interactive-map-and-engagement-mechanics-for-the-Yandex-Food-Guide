#include "restaurant_repository.hpp"

namespace service {

RestaurantRepository::RestaurantRepository(const userver::storages::postgres::ClusterPtr& pg_cluster) :
    pg_cluster_(pg_cluster),
    kTableName("guide.places")
    {}

std::vector<TRestaurant> RestaurantRepository::GetAll() {
    const auto& restaurants = pg_cluster_->Execute(
        userver::storages::postgres::ClusterHostType::kSlave,
        R"(SELECT * FROM )" + kTableName + ';'
    );
    return restaurants.AsContainer<std::vector<TRestaurant>>(userver::storages::postgres::kRowTag);
}

TRestaurant RestaurantRepository::GetById(const std::string& id) {
    return {};
}

std::vector<TRestaurant> RestaurantRepository::GetByFilter(const TRestaurantFilter& filter) {
    const auto& restaurants = pg_cluster_->Execute(
        userver::storages::postgres::ClusterHostType::kSlave,
        R"(SELECT * FROM )" + kTableName +
        R"( WHERE lat BETWEEN $1 AND $2)"
        R"( AND lon BETWEEN $3 AND $4;)",
        filter.lower_left_corner.lat,
        filter.top_right_corner.lat,
        filter.lower_left_corner.lon,
        filter.top_right_corner.lon
    );
    return restaurants.AsContainer<std::vector<TRestaurant>>(userver::storages::postgres::kRowTag);
}

} // service