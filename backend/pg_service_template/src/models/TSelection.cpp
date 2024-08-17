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
    item["picture"] = selection.picture;
//    item["is_collection"] = selection.owner_id;
   
    return item.ExtractValue();
}

std::tuple<
    boost::uuids::uuid&,
    std::string&,
    std::string&,
    std::optional<boost::uuids::uuid>&,
    std::string&
    > TSelection::Introspect()
{
    return std::tie(
          id,
          name,
          description,
          owner_id,
          picture
    );
}

}