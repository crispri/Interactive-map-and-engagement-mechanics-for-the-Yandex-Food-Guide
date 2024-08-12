#include "OpenTimeRestaurantFilterJSON.hpp"

#include <string>

#include <lib/error_description.hpp>

#include <userver/utils/time_of_day.hpp>


namespace service {


    userver::utils::datetime::TimeOfDay<std::chrono::seconds> Parse(
            const userver::formats::json::Value& json,
            userver::formats::parse::To< userver::utils::datetime::TimeOfDay<std::chrono::seconds> >
    )
    {
        return userver::utils::datetime::TimeOfDay<std::chrono::seconds>{
//                json["lat"].As<double>(),
//                json["lon"].As<double>()
        };
    }

    const std::string OpenTimeRestaurantFilterJSON::kFieldName_ = "open_time";
    const std::unordered_map<std::string, std::string> OpenTimeRestaurantFilterJSON::kCorrectOperators_ = {
            {"lt", "<"},
            {"gt", ">"},
            {"ge", ">="},
            {"le", "<="},
            {"eq", "="},
    };

    std::variant<std::string, ErrorDescriprion> OpenTimeRestaurantFilterJSON::BuildSQLFilter(
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
//        const std::regex time_regex("[a-z]+\\.txt")
//        if (!JSON["value"][0].IsTime()) {
//            return ErrorDescriprion::kInvalidValueType;
//        }
        params.PushBack(JSON["value"][0].As< userver::utils::datetime::TimeOfDay<std::chrono::seconds> >());
        return fmt::format(" {} {} ${} ", kFieldName_, kCorrectOperators_.at(op), params.Size());
    }


} // namespace service