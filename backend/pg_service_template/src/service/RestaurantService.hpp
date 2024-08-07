#pragma once

#include <repository/PgRestaurantRepository.hpp>

namespace service {


class RestaurantService {
  std::unique_ptr<IRestaurantRepository> repository_;

public:
  RestaurantService(const userver::storages::postgres::ClusterPtr& pg_cluster);

  std::vector<TRestaurant> GetAll();
  std::optional<TRestaurant> GetById(const boost::uuids::uuid& id);
  std::vector<TRestaurant> GetByFilter(const TRestaurantFilter& filter);
};


} // namespace service