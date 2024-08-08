#pragma once

#include <userver/components/component_list.hpp>
#include <service/RestaurantService.hpp>
#include <userver/components/component_base.hpp>

namespace service {


class RestaurantServiceComponent final : public userver::components::ComponentBase {

public:
    static constexpr std::string_view kName = "restaurant-service-component";

    RestaurantServiceComponent(
            const userver::components::ComponentConfig &config,
            const userver::components::ComponentContext &context
    );

    const RestaurantService &GetService() const;

private:
    RestaurantService restaurant_service_;

};

void AppendRestaurantService(
    userver::components::ComponentList& component_list
);



} // namespace service

