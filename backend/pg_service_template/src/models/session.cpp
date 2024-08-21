#include "session.hpp"
#include <lib/time_parser.hpp>
#include <boost/uuid/uuid_io.hpp>

namespace service{

    userver::formats::json::Value Serialize( //из объекта в json
    const TSession& data,
    userver::formats::serialize::To<userver::formats::json::Value>){

        userver::formats::json::ValueBuilder item;
        item["user_id"] = boost::uuids::to_string(data.user_id);
        item["session_id"] = boost::uuids::to_string(data.session_id);
        item["expiration_time"] = TimeParser::ParseDate(data.expiration_time);
   
    return item.ExtractValue();
    }
}