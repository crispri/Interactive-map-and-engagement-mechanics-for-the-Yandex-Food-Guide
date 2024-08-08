#include "RestaurantService.hpp"


#include <userver/components/component_list.hpp>


namespace service {


RestaurantService::RestaurantService(
    const userver::storages::postgres::ClusterPtr& pg_cluster
) :
  repository_(std::make_shared<PgRestaurantRepository>(pg_cluster))
  {}

std::vector<TRestaurant> RestaurantService::GetAll() const {
    return repository_->GetAll();
}

std::optional<TRestaurant> RestaurantService::GetById(const boost::uuids::uuid& id) const {
    return repository_->GetById(id);
}

std::vector<TRestaurant> RestaurantService::GetByFilter(const TRestaurantFilter& filter) const {
    return repository_->GetByFilter(filter);
}


} // namespace service