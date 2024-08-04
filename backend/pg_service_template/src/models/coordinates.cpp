#include "coordinates.hpp"


namespace service {


userver::formats::json::Value Serialize(const TCoordinates& coordinates,
                                        userver::formats::serialize::To<userver::formats::json::Value>) {
    userver::formats::json::ValueBuilder item;
    item["lat"] = coordinates.lat;
    item["lon"] = coordinates.lon;
    return item.ExtractValue();
}

TCoordinates Parse(
    const userver::formats::json::Value& json,
    userver::formats::parse::To<TCoordinates>
)
{
    return TCoordinates{
        json["lat"].As<double>(),
        json["lon"].As<double>()
    };
}


} // namespace service