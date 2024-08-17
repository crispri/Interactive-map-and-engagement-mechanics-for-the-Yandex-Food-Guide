#include "PgRestaurantRepository.hpp"

namespace service {


PgRestaurantRepository::PgRestaurantRepository(const userver::storages::postgres::ClusterPtr& pg_cluster) :
    pg_cluster_(pg_cluster),
    kTableName_("guide.places")
    {}

std::vector<TRestaurant> PgRestaurantRepository::GetAll(const boost::uuids::uuid& user_id) {
    const auto& restaurants = pg_cluster_->Execute(
        userver::storages::postgres::ClusterHostType::kSlave,
        R"(SELECT * FROM )" + kTableName_ + ';'
    );
    return restaurants.AsContainer<std::vector<TRestaurant>>(userver::storages::postgres::kRowTag);
}

std::optional<TRestaurant> PgRestaurantRepository::GetById(const boost::uuids::uuid& restaurant_id, const boost::uuids::uuid& user_id) {
    const auto& restaurant = pg_cluster_->Execute(
        userver::storages::postgres::ClusterHostType::kSlave,
        R"(SELECT * FROM )" + kTableName_ +
        R"( WHERE id = $1;)",
        restaurant_id
    );
    if (restaurant.IsEmpty()) {
        return std::nullopt;
    }
    return restaurant[0].As<TRestaurant>(userver::storages::postgres::kRowTag);
}

std::vector<TRestaurant> PgRestaurantRepository::GetByFilter(const TRestaurantFilter& filter, const boost::uuids::uuid& user_id) {
    filter.filter_params.PushBack(filter.lower_left_corner.lat);
    filter.filter_params.PushBack(filter.top_right_corner.lat);
    filter.filter_params.PushBack(filter.lower_left_corner.lon);
    filter.filter_params.PushBack(filter.top_right_corner.lon);

    const auto& restaurants_in_collection = pg_cluster_->Execute(
        userver::storages::postgres::ClusterHostType::kSlave,
        R"( SELECT place_id FROM guide.places_selections )"
        R"( WHERE selection_id IN )"
        R"( (SELECT id FROM guide.selections WHERE owner_id = $1); )",
        user_id
    ).AsContainer< std::set<boost::uuids::uuid> >();

    const std::string& query =
            R"(SELECT * FROM )" + kTableName_ +
            R"( WHERE )" +
            filter.filter_string +
            fmt::format(" lat BETWEEN ${} AND ${} ", filter.filter_params.Size() - 3, filter.filter_params.Size() - 2) +
            fmt::format(" AND lon BETWEEN ${} AND ${};", filter.filter_params.Size() - 1, filter.filter_params.Size());

    const auto& restaurants = pg_cluster_->Execute(
        userver::storages::postgres::ClusterHostType::kSlave,
        query,
        filter.filter_params
    );

    auto result = restaurants.AsContainer<std::vector<TRestaurant>>(userver::storages::postgres::kRowTag);
    for (auto& restaurant : result) {
        restaurant.in_collection = restaurants_in_collection.count(restaurant.id);
    }
    return result;
}


} // service