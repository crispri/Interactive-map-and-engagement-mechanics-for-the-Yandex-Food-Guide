#pragma once

#include <iterator>
#include <string>
#include <string_view>
#include <variant>
#include <lib/error_description.hpp>
#include <userver/formats/json/value_builder.hpp>

namespace service {

struct IRestaurantFilterJSON {
    virtual std::variant<std::string, ErrorDescriprion> BuildSQLFilter(std::size_t) = 0;
};

} // namespace service