#pragma once

#include <boost/uuid/uuid.hpp>
#include <userver/formats/json/value_builder.hpp>


namespace service {

struct TSelection {
    boost::uuids::uuid id;
    std::string name;
    std::string description;
    short is_public;   

    std::tuple<
        boost::uuids::uuid&,
        std::string&,
        std::string&,
        short&
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