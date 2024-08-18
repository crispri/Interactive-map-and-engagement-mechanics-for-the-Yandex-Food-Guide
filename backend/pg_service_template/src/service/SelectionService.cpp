#include "SelectionService.hpp"

#include <boost/uuid/uuid.hpp>
#include <userver/storages/postgres/component.hpp>
#include <userver/components/component.hpp>
#include <models/TRestaurant.hpp>
#include <models/TSelection.hpp>

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

std::vector<TSelection> SelectionService::GetAll(const boost::uuids::uuid& user_id, bool return_collections) {
    return repository_->GetAll(user_id, return_collections);
}

std::vector<TRestaurant> SelectionService::GetById(const boost::uuids::uuid& selection_id, const boost::uuids::uuid& user_id) {
    return repository_->GetById(selection_id, user_id);
}

boost::uuids::uuid SelectionService::CreateCollection(const boost::uuids::uuid& user_id, const std::string& name, const std::string& description) {
    return repository_->CreateCollection(user_id, name, description);
}


void SelectionService::InsertIntoCollection(const boost::uuids::uuid& user_id, const boost::uuids::uuid& collection_id, const boost::uuids::uuid& restaurant_id) {
    repository_->InsertIntoCollection(user_id, collection_id, restaurant_id);
}

void AppendSelectionService(userver::components::ComponentList& component_list) {
    component_list.Append<SelectionService>();
}

} // namespace service