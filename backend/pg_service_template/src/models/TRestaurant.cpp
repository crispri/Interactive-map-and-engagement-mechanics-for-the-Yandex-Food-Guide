#include "TRestaurant.hpp"
#include <userver/formats/json/value_builder.hpp>
#include <lib/time_parser.hpp>
#include <boost/uuid/uuid_io.hpp>
#include <userver/logging/log.hpp>


namespace service {

userver::formats::json::Value Serialize(
    const TRestaurant& restaurant,
    userver::formats::serialize::To<userver::formats::json::Value>
)
{
    userver::formats::json::ValueBuilder item;
    item["id"] = boost::uuids::to_string(restaurant.id);
    item["coordinates"] = restaurant.coordinates;
    item["name"] = restaurant.name;
    item["description"] = restaurant.description;
    item["address"] = restaurant.address;
    item["is_approved"] = restaurant.is_approved;
    item["rating"] = restaurant.rating;
    item["price_lower_bound"] = restaurant.price_lower_bound;
    item["price_upper_bound"] = restaurant.price_upper_bound;
    item["open_time"] = TimeParser::Parse(restaurant.open_time);
    item["close_time"] = TimeParser::Parse(restaurant.close_time);
    item["tags"].Resize(0);
    if (restaurant.tags) {
        for (const auto& tag : restaurant.tags.value()) {
            item["tags"].PushBack(userver::formats::json::ValueBuilder{tag});
        }
    }
    item["in_collection"] = restaurant.in_collection;

    return item.ExtractValue();
}

std::tuple<
    boost::uuids::uuid&,
    double&,
    double&,
    std::string&,
    std::string&,
    bool&,
    double&,
    int&,
    int&,
    userver::utils::datetime::TimeOfDay<std::chrono::seconds>&,
    userver::utils::datetime::TimeOfDay<std::chrono::seconds>&,
    std::string&,
    std::optional<std::vector<std::string>>&
    > TRestaurant::Introspect()
{
    return std::tie(
          id,
          coordinates.lat,
          coordinates.lon,
          name,
          description,
          is_approved,
          rating,
          price_lower_bound,
          price_upper_bound,
          open_time,
          close_time,
          address,
          tags
  );
}

} // namespace service