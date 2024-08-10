#pragma once

#include <userver/formats/json/value_builder.hpp>


namespace service {

struct TSelection {
    int id;
    std::string name;
    std::string description;
    bool is_public;   
};

userver::formats::json::Value Serialize(
    const TSelection& data,
    userver::formats::serialize::To<userver::formats::json::Value>
);

/*
    Пока считаем, что нам не надо парсить
    json в TSelection.
*/
TSelection Parse(
    const userver::formats::json::Value& json,
    userver::formats::parse::To<TSelection>
);

}