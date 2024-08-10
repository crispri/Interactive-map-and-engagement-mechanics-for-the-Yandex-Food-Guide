#pragma once

#include <repository/PgRestaurantRepository.hpp>
#include <userver/components/component_list.hpp>
#include <userver/components/component_base.hpp>

namespace service {


class RestaurantService final : public userver::components::ComponentBase {
    std::shared_ptr<IRestaurantRepository> repository_;

public:
    static constexpr std::string_view kName = "restaurant-service-component";

    RestaurantService(
        const userver::components::ComponentConfig &config,
        const userver::components::ComponentContext &context
    );

    std::vector<TRestaurant> GetAll();
    std::optional<TRestaurant> GetById(const boost::uuids::uuid& id);
    std::vector<TRestaurant> GetByFilter(const TRestaurantFilter& filter);
};


void AppendRestaurantService(
    userver::components::ComponentList& component_list
);


} // namespace service