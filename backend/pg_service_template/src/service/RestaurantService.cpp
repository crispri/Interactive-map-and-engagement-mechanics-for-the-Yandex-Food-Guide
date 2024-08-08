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

std::vector<TRestaurant> RestaurantService::GetAll() {
    return repository_->GetAll();
}

std::optional<TRestaurant> RestaurantService::GetById(const boost::uuids::uuid& id) {
    return repository_->GetById(id);
}

std::vector<TRestaurant> RestaurantService::GetByFilter(const TRestaurantFilter& filter) {
    return repository_->GetByFilter(filter);
}

void AppendRestaurantService(userver::components::ComponentList& component_list) {
    component_list.Append<RestaurantService>();
}


} // namespace service