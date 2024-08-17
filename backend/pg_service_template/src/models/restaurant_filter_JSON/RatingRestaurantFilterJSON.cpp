#include "RatingRestaurantFilterJSON.hpp"
#include <iterator>
#include <string>
#include <string_view>
#include <vector>
#include "lib/error_description.hpp"

namespace service {

const std::string RatingRestaurantFilter::kFieldName_ = "rating";

const std::unordered_map<std::string, std::string> RatingRestaurantFilter::kCorrectOperators_ = {
    {"lt", "<"},
    {"gt", ">"},
    {"ge", ">="},
    {"le", "<="},
    {"eq", "="},
};

RatingRestaurantFilter::RatingRestaurantFilter(const userver::formats::json::Value& JSON) : kJSON_(JSON) {}

std::variant<std::string, ErrorDescriprion> RatingRestaurantFilter::BuildSQLFilter(std::size_t start) {
    if (!kJSON_.HasMember("operator")) {
        return ErrorDescriprion::kOperatorNotSpecified;
    }
    if (!kJSON_.HasMember("value")) {
        return ErrorDescriprion::kValueNotSpecified;
    }
    if (!kJSON_["value"].IsArray()) {
        return ErrorDescriprion::kValueIsNotArray;
    }
    if (kJSON_["value"].GetSize() != kValueArraySize_) {
        return ErrorDescriprion::kInvalidValueArraySize;
    }
    const auto& op = kJSON_["operator"].As<std::string>();
    if (!kCorrectOperators_.count(op)) {
        return ErrorDescriprion::kInvalidOperator;
    }
    return R"( WHERE )" + kFieldName_ + kCorrectOperators_.at(op); + " $" + std::to_string(start);
}

} // namespace service