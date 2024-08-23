#include "RestaurantService.hpp"

#include <userver/storages/postgres/component.hpp>
#include <userver/components/component.hpp>

namespace service {


RestaurantService::RestaurantService(
    const userver::components::ComponentConfig &config,
    const userver::components::ComponentContext &context
) :
    userver::components::ComponentBase(
        config,
        context
    ),
    repository_(
        std::make_shared<PgRestaurantRepository>(
            context
            .FindComponent<userver::components::Postgres>("postgres-db-1")
            .GetCluster()
        )
    )
{}

std::vector<TRestaurant> RestaurantService::GetAll(const boost::uuids::uuid& user_id) {
    return repository_->GetAll(user_id);
}

std::optional<TRestaurant> RestaurantService::GetById(const boost::uuids::uuid& restaurant_id, const boost::uuids::uuid& user_id) {
    return repository_->GetById(restaurant_id, user_id);
}

std::vector<TRestaurant> RestaurantService::GetByFilter(const TRestaurantFilter& filter, const boost::uuids::uuid& user_id) {
    return repository_->GetByFilter(filter, user_id);
}

void AppendRestaurantService(userver::components::ComponentList& component_list) {
    component_list.Append<RestaurantService>();
}


} // namespace service