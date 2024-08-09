#include "restaurant_filter.hpp"

namespace service {

TRestaurantFilter::TRestaurantFilter(
    service::TCoordinates lower_left_corner,
    service::TCoordinates top_right_corner
) :
    lower_left_corner(lower_left_corner),
    top_right_corner(top_right_corner)
{}

} // namespace service