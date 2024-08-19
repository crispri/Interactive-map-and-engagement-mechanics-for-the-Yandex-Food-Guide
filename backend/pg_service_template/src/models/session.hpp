#include <boost/uuid/uuid.hpp>
#include <chrono>
#include <userver/formats/json/value_builder.hpp>

namespace service{   

    struct TSession {
        boost::uuids::uuid user_id;
        boost::uuids::uuid session_id;
        std::chrono::system_clock::time_point expiration_time;
    };

    userver::formats::json::Value Serialize( //из объекта в json
    const TSession& data,
    userver::formats::serialize::To<userver::formats::json::Value>);


    TSession Parse( //из json в объект
    const userver::formats::json::Value& json,
    userver::formats::parse::To<TSession>);
    
}