#include "SelectionService.hpp"

#include <userver/storages/postgres/component.hpp>
#include <userver/components/component.hpp>
#include "models/restaurant.hpp"
#include "models/selection.hpp"
#include "service/SelectionService.hpp"

namespace service {


SelectionService::SelectionService(
    const userver::components::ComponentConfig &config,
    const userver::components::ComponentContext &context
) :
    userver::components::ComponentBase(
        config,
        context
    ),
    repository_(
        std::make_shared<PgSelectionRepository>(
            context
            .FindComponent<userver::components::Postgres>("postgres-db-1")
            .GetCluster()
        )
    )
{}

std::vector<TSelection> SelectionService::GetAll() {
    return repository_->GetAll();
}

std::vector<TRestaurant> SelectionService::GetById(const boost::uuids::uuid& id) {
    return repository_->GetById(id);
}



void AppendSelectionService(userver::components::ComponentList& component_list) {
    component_list.Append<SelectionService>();
}


} // namespace service