#include "CloseTimeRestaurantFilterJSON.hpp"

#include <string>
#include <regex>

#include <lib/error_description.hpp>

#include <userver/utils/time_of_day.hpp>
#include <userver/formats/parse/time_of_day.hpp>


namespace service {


    const std::string CloseTimeRestaurantFilterJSON::kFieldName_ = "close_time";
    const std::unordered_map<std::string, std::string> CloseTimeRestaurantFilterJSON::kCorrectOperators_ = {
            {"lt", "<"},
            {"gt", ">"},
            {"ge", ">="},
            {"le", "<="},
            {"eq", "="},
    };

    std::variant<std::string, ErrorDescriprion> CloseTimeRestaurantFilterJSON::BuildSQLFilter(
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
        const std::regex time_regex(R"(([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9])");
        if (!std::regex_match(JSON["value"][0].As<std::string>(), time_regex)) {
            return ErrorDescriprion::kInvalidValueType;
        }
        params.PushBack(JSON["value"][0].As< userver::utils::datetime::TimeOfDay<std::chrono::seconds> >());
        return fmt::format(" {} {} ${} ", kFieldName_, kCorrectOperators_.at(op), params.Size());
    }


} // namespace service