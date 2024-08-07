#pragma once
#include "coordinates.hpp"

namespace service {

struct TRestaurantFilter {
    TCoordinates lower_left_corner;
    TCoordinates top_right_corner;

    TRestaurantFilter(TCoordinates, TCoordinates);
};

};