#pragma once

#include <repository/PgRestaurantRepository.hpp>

namespace service {


class RestaurantService {
    std::shared_ptr<IRestaurantRepository> repository_;

public:
    explicit RestaurantService(const userver::storages::postgres::ClusterPtr& pg_cluster);
    RestaurantService(const RestaurantService& other) = default;

    std::vector<TRestaurant> GetAll() const;
    std::optional<TRestaurant> GetById(const boost::uuids::uuid& id) const;
    std::vector<TRestaurant> GetByFilter(const TRestaurantFilter& filter) const;
};


} // namespace service