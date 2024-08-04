#include "restaurant.hpp"


namespace service {


userver::formats::json::Value Serialize(
    const TRestaurant& restaurant,
    userver::formats::serialize::To<userver::formats::json::Value>
)
{
    userver::formats::json::ValueBuilder item;
    item["coordinates"] = restaurant.coordinates;
    item["name"] = restaurant.name;
    item["description"] = restaurant.description;
    item["address"] = restaurant.address;
    item["is_approved"] = restaurant.is_approved;
    item["rating"] = restaurant.rating;
    item["price_lower_bound"] = restaurant.price_lower_bound;
    item["price_upper_bound"] = restaurant.price_upper_bound;
    item["is_favorite"] = restaurant.is_favorite;

    return item.ExtractValue();
}

// TRestaurant Parse(
//     const userver::formats::json::Value& json,
//     userver::formats::parse::To<TRestaurant>
// ) = delete;


} // namespace service