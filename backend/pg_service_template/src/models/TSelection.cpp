#include "TSelection.hpp"

#include <boost/uuid/uuid_io.hpp>

namespace service {

userver::formats::json::Value Serialize(
    const TSelection& selection,
    userver::formats::serialize::To<userver::formats::json::Value>
)
{
    userver::formats::json::ValueBuilder item;
    item["id"] = boost::uuids::to_string(selection.id);
    item["name"] = selection.name;
    item["description"] = selection.description;
    item["is_public"] = selection.is_public;
   
    return item.ExtractValue();
}

std::tuple<
    boost::uuids::uuid&,
    std::string&,
    std::string&,
    short&
    > TSelection::Introspect()
{
    return std::tie(
          id,
          name,
          description,
          is_public
    );
}

}