#pragma once

#include <vector>
#include <string>

#include <models/restaurant.hpp>
#include <models/restaurant_filter.hpp>


namespace service {


struct IRestaurantRepository {
    virtual std::vector<TRestaurant> GetAll() = 0;
    virtual std::optional<TRestaurant> GetById(const boost::uuids::uuid&) = 0;
    virtual std::vector<TRestaurant> GetByFilter(const TRestaurantFilter&) = 0;

    virtual ~IRestaurantRepository() = default;
};


} // namespace service