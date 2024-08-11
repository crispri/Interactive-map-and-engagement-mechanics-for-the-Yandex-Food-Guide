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
    filter.filter_params.PushBack(filter.lower_left_corner.lat);
    filter.filter_params.PushBack(filter.top_right_corner.lat);
    filter.filter_params.PushBack(filter.lower_left_corner.lon);
    filter.filter_params.PushBack(filter.top_right_corner.lon);

    const std::string& query =
            R"(SELECT * FROM )" + kTableName_ +
            R"( WHERE )" +
            filter.filter_string +
            fmt::format(" lat BETWEEN ${} AND ${} ", filter.filter_params.Size() - 3, filter.filter_params.Size() - 2) +
            fmt::format(" AND lon BETWEEN ${} AND ${};", filter.filter_params.Size() - 1, filter.filter_params.Size());

    LOG_ERROR() << "QUERY = ." << query << ".";

    const auto& restaurants = pg_cluster_->Execute(
        userver::storages::postgres::ClusterHostType::kSlave,
        query,
        filter.filter_params
    );
    return restaurants.AsContainer<std::vector<TRestaurant>>(userver::storages::postgres::kRowTag);
}

} // service