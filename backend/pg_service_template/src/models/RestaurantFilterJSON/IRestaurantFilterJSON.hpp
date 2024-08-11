#pragma once

#include <iterator>
#include <string>
#include <string_view>
#include <variant>
#include <lib/error_description.hpp>
#include <userver/formats/json/value_builder.hpp>
#include <userver/storages/postgres/parameter_store.hpp>

namespace service {

struct IRestaurantFilterJSON {
    virtual std::variant<std::string, ErrorDescriprion> BuildSQLFilter(
        userver::storages::postgres::ParameterStore&,
        const userver::formats::json::Value&
    ) = 0;
};

} // namespace service