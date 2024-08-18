#pragma once

#include <vector>
#include <string>

#include <models/TRestaurant.hpp>
#include <models/TRestaurantFilter.hpp>


namespace service {

struct IRestaurantRepository  {

    virtual std::vector<TRestaurant> GetAll(const boost::uuids::uuid& user_id) = 0;
    virtual std::optional<TRestaurant> GetById(const boost::uuids::uuid& restaurant_id, const boost::uuids::uuid& user_id) = 0;
    virtual std::vector<TRestaurant> GetByFilter(const TRestaurantFilter& filter, const boost::uuids::uuid& user_id) = 0;

    virtual ~IRestaurantRepository() = default;
};


} // namespace service