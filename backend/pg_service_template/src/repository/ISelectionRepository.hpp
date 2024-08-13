#pragma once

#include <vector>
#include <string>

#include <models/TSelection.hpp>
#include <models/TRestaurant.hpp>



namespace service {

struct ISelectionRepository  {
    virtual std::vector<TSelection> GetAll() = 0;
    virtual std::vector<TRestaurant> GetById(const boost::uuids::uuid&) = 0;
    virtual ~ISelectionRepository() = default;
};


} // namespace service