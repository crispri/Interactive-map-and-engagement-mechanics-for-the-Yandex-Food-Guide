#pragma once

#include <vector>
#include <string>

#include <models/selection.hpp>
#include <models/restaurant.hpp>



namespace service {

struct ISelectionRepository  {
    virtual std::vector<TSelection> GetAll() = 0;
    virtual std::vector<TRestaurant> GetById(const boost::uuids::uuid&) = 0;
    virtual ~ISelectionRepository() = default;
};


} // namespace service