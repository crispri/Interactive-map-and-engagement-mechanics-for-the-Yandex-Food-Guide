#include "RestaurantService.hpp"

namespace service {


RestaurantService::RestaurantService(
    const userver::storages::postgres::ClusterPtr& pg_cluster
) :
  repository_(std::make_unique<PgRestaurantRepository>(pg_cluster))
  {}

std::vector<TRestaurant> RestaurantService::GetAll() {
    return repository_->GetAll();
}

std::optional<TRestaurant> RestaurantService::GetById(const boost::uuids::uuid& id) {
    return repository_->GetById(id);
}

std::vector<TRestaurant> RestaurantService::GetByFilter(const TRestaurantFilter& filter) {
    return repository_->GetByFilter(filter);
}


} // namespace service