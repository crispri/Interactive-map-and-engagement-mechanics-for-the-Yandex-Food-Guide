#pragma once

#include <userver/formats/json/value_builder.hpp>
#include <userver/utils/time_of_day.hpp>
#include "coordinates.hpp"


namespace service {

struct TRestaurant {
    std::string id;
    TCoordinates coordinates;
    std::string name;
    std::string description;
    bool is_approved;
    double rating;
    int price_lower_bound;
    int price_upper_bound;
    userver::utils::datetime::TimeOfDay<std::chrono::seconds> open_time;
    userver::utils::datetime::TimeOfDay<std::chrono::seconds> close_time;
    std::string address;
    std::optional<std::vector<short>> tags;
    bool is_favorite;

    std::tuple<
        std::string&,
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
        std::optional<std::vector<short>>&,
        bool&
    > Introspect();
};

userver::formats::json::Value Serialize(
    const TRestaurant& data,
    userver::formats::serialize::To<userver::formats::json::Value>
);

}