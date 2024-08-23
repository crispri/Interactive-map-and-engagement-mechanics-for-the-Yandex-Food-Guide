#pragma once

#include <vector>
#include <string>

#include <boost/uuid/uuid_io.hpp>
#include <models/TRestaurant.hpp>
#include <models/TRestaurantFilter.hpp>


namespace service {

struct IMLRepository  {

  virtual boost::uuids::uuid GetUserIdByAuthToken(const boost::uuids::uuid&) = 0;

  virtual ~IMLRepository() = default;
};


} // namespace service