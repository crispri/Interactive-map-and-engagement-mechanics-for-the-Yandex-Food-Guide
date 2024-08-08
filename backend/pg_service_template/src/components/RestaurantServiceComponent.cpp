#include "RestaurantServiceComponent.hpp"

#include <userver/storages/postgres/component.hpp>
#include <userver/components/component.hpp>


namespace service {


RestaurantServiceComponent::RestaurantServiceComponent(
    const userver::components::ComponentConfig &config,
    const userver::components::ComponentContext &context
) :
    userver::components::ComponentBase(
        config,
        context
    ),
    restaurant_service_(
        context
        .FindComponent<userver::components::Postgres>("postgres-db-1")
        .GetCluster()
    ) {}

const RestaurantService& RestaurantServiceComponent::GetService() const {
    return restaurant_service_;
}


void AppendRestaurantService(
    userver::components::ComponentList& component_list
)
{
    component_list.Append<RestaurantServiceComponent>();
}



} // namespace service