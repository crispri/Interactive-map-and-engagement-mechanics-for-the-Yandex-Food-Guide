#include "TSelection.hpp"


namespace service {


userver::formats::json::Value Serialize(
    const TSelection& selection,
    userver::formats::serialize::To<userver::formats::json::Value>
)
{
    userver::formats::json::ValueBuilder item;
    item["id"] = selection.id;
    item["name"] = selection.name;
    item["description"] = selection.description;
    item["is_public"] = selection.is_public;
   
    return item.ExtractValue();
}
}