#include "PgRestaurantRepository.hpp"

namespace service {


PgRestaurantRepository::PgRestaurantRepository(const userver::storages::postgres::ClusterPtr& pg_cluster) :
    pg_cluster_(pg_cluster),
    kTableName_("guide.places")
    {}

std::vector<TRestaurant> PgRestaurantRepository::GetAll() {
    const auto& restaurants = pg_cluster_->Execute(
        userver::storages::postgres::ClusterHostType::kSlave,
        R"(SELECT * FROM )" + kTableName_ + ';'
    );
    return restaurants.AsContainer<std::vector<TRestaurant>>(userver::storages::postgres::kRowTag);
}
std::optional<TRestaurant> PgRestaurantRepository::GetById(const boost::uuids::uuid& id) {
    const auto& restaurant = pg_cluster_->Execute(
        userver::storages::postgres::ClusterHostType::kSlave,
        R"(SELECT * FROM )" + kTableName_ +
        R"( WHERE id = $1;)",
        id
    );
    if (restaurant.IsEmpty()) {
        return std::nullopt;
    }
    return restaurant[0].As<TRestaurant>(userver::storages::postgres::kRowTag);
}

std::vector<TRestaurant> PgRestaurantRepository::GetByFilter(const TRestaurantFilter& filter) {
    const auto& restaurants = pg_cluster_->Execute(
        userver::storages::postgres::ClusterHostType::kSlave,
        R"(SELECT * FROM )" + kTableName_ +
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