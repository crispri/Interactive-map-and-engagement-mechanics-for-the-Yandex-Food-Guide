#pragma once

#include <boost/uuid/uuid.hpp>
#include <string>
#include <userver/formats/json/value_builder.hpp>


namespace service {

struct TSelection {
    boost::uuids::uuid id;
    std::string name;
    std::string description;
    std::optional<boost::uuids::uuid> owner_id;
    std::optional<std::string> picture;
    std::optional<std::string> link;
    std::optional<std::string> pre_created_collection_name;
    std::tuple<
        boost::uuids::uuid&,
        std::string&,
        std::string&,
        std::optional<boost::uuids::uuid>&,
        std::optional<std::string>&,
        std::optional<std::string>&,
        std::optional<std::string>&
    > Introspect();
};

userver::formats::json::Value Serialize( //из объекта в json
    const TSelection& data,
    userver::formats::serialize::To<userver::formats::json::Value>
);


TSelection Parse( //из json в объект
    const userver::formats::json::Value& json,
    userver::formats::parse::To<TSelection>
);


}