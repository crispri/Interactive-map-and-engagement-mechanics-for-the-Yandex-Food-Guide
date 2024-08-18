#include "PriceLBRestaurantFilterJSON.hpp"

#include <string>

#include <lib/error_description.hpp>

namespace service {


    const std::string PriceLBRestaurantFilterJSON::kFieldName_ = "price_lower_bound";
    const std::unordered_map<std::string, std::string> PriceLBRestaurantFilterJSON::kCorrectOperators_ = {
            {"lt", "<"},
            {"gt", ">"},
            {"ge", ">="},
            {"le", "<="},
            {"eq", "="},
    };

    std::variant<std::string, ErrorDescriprion> PriceLBRestaurantFilterJSON::BuildSQLFilter(
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
        if (!JSON["value"][0].IsInt()) {
            return ErrorDescriprion::kInvalidValueType;
        }
        params.PushBack(JSON["value"][0].As<int>());
        return fmt::format(" {} {} ${} ", kFieldName_, kCorrectOperators_.at(op), params.Size());
    }


} // namespace service