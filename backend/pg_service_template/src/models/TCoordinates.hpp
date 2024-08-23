#pragma once

#include <userver/formats/json/value_builder.hpp>

namespace service {


struct TCoordinates {
    double lat;
    double lon;
};

userver::formats::json::Value Serialize(
    const TCoordinates& data,
    userver::formats::serialize::To<userver::formats::json::Value>
);

TCoordinates Parse(
    const userver::formats::json::Value& json,
    userver::formats::parse::To<TCoordinates>
);


} // namespace service