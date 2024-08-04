#pragma once

#include <userver/formats/json/value_builder.hpp>
#include "coordinates.hpp"

namespace service {

struct TRestaurant {
    TCoordinates coordinates;
    std::string name;
    std::string description;
    std::string address;
    bool is_approved;
    double rating;
    int price_lower_bound;
    int price_upper_bound;
    std::vector<std::string> tags;
    bool is_favorite;
};

userver::formats::json::Value Serialize(
    const TRestaurant& data,
    userver::formats::serialize::To<userver::formats::json::Value>
);

/*
    Пока считаем, что нам не надо парсить
    json в TRestaurant.
*/
TRestaurant Parse(
    const userver::formats::json::Value& json,
    userver::formats::parse::To<TRestaurant>
);

}