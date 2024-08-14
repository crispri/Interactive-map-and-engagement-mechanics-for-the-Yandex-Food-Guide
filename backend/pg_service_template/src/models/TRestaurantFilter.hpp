#pragma once
#include "TCoordinates.hpp"

#include <userver/storages/postgres/parameter_store.hpp>

namespace service {

struct TRestaurantFilter {
    TCoordinates lower_left_corner;
    TCoordinates top_right_corner;
    userver::storages::postgres::ParameterStore& filter_params;
    const std::string& filter_string;

    TRestaurantFilter(TCoordinates, TCoordinates, userver::storages::postgres::ParameterStore&, const std::string&);
};

};