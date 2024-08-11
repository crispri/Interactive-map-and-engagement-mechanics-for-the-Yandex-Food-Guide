#pragma once

#include <repository/PgSelectionRepository.hpp>
#include <userver/components/component_list.hpp>
#include <userver/components/component_base.hpp>
#include "models/restaurant.hpp"

namespace service {


class SelectionService final : public userver::components::ComponentBase {
    std::shared_ptr<ISelectionRepository> repository_;

public:
    static constexpr std::string_view kName = "selection-service-component";

    SelectionService(
        const userver::components::ComponentConfig &config,
        const userver::components::ComponentContext &context
    );

    std::vector<TSelection> GetAll();
    std::vector<TRestaurant> GetById(const boost::uuids::uuid& id);
    
};


void AppendSelectionService(
    userver::components::ComponentList& component_list
);

} // namespace service