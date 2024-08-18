#include "RatingRestaurantFilterJSON.hpp"

#include <string>

#include <lib/error_description.hpp>

namespace service {


const std::string RatingRestaurantFilterJSON::kFieldName_ = "rating";
const std::unordered_map<std::string, std::string> RatingRestaurantFilterJSON::kCorrectOperators_ = {
    {"lt", "<"},
    {"gt", ">"},
    {"ge", ">="},
    {"le", "<="},
    {"eq", "="},
};

std::variant<std::string, ErrorDescriprion> RatingRestaurantFilterJSON::BuildSQLFilter(
        userver::storages::postgres::ParameterStore& params,
        const userver::formats::json::Value& JSON
)
{
    if (JSON["value"].GetSize() != kValueArraySize_) {
        return ErrorDescriprion::kInvalidValueArraySize;
    }
    const auto& op = JSON["operator"].As<std::string>();
    if (!kCorrectOperators_.count(op)) {
        return ErrorDescriprion::kInvalidOperator;
    }
    if (!JSON["value"][0].IsDouble()) {
        return ErrorDescriprion::kInvalidValueType;
    }
    params.PushBack(JSON["value"][0].As<double>());
    return fmt::format(" {} {} ${} ", kFieldName_, kCorrectOperators_.at(op), params.Size());
}


} // namespace service