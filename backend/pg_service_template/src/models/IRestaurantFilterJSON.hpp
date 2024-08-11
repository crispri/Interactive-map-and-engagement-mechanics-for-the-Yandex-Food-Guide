#pragma once

#include <string>

namespace service {

struct IRestaurantFilterJSON {
    virtual std::string BuildSQLFilter() = 0;
};

} // namespace service