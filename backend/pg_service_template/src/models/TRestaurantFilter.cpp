#include "TRestaurantFilter.hpp"

namespace service {


TRestaurantFilter::TRestaurantFilter(
    service::TCoordinates lower_left_corner,
    service::TCoordinates top_right_corner,
    userver::storages::postgres::ParameterStore& filter_params,
    const std::string& filter_string,
    bool only_collection
) :
    lower_left_corner(lower_left_corner),
    top_right_corner(top_right_corner),
    filter_params(filter_params),
    filter_string(filter_string),
    only_collection(only_collection)

{}

} // namespace service