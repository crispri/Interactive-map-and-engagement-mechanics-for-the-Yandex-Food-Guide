#include "TagRestaurantFilterJSON.hpp"

#include <string>

#include <lib/error_description.hpp>

#include <userver/utils/time_of_day.hpp>
#include <userver/formats/parse/common_containers.hpp>


namespace service {


    const std::string TagRestaurantFilterJSON::kFieldName_ = "tags";
    const std::unordered_map<std::string, std::string> TagRestaurantFilterJSON::kCorrectOperators_ = {
            {"in", " {} && ${}::varchar(50)[] "},
            {"not in", " NOT ( {} && ${}::varchar(50)[] )"},
    };
    const std::unordered_set<std::string> TagRestaurantFilterJSON::kCorrectTags_ = {
            "ULTIMA GUIDE",
            "Завтрак",
            "Бизнес-ланч",
            "Выпить кофе",
            "Бар",
            "Семейный ужин с детьми",
            "В тренде",
            "Ресторан",
            "Открытая кухня"
    };

    std::variant<std::string, ErrorDescriprion> TagRestaurantFilterJSON::BuildSQLFilter(
            userver::storages::postgres::ParameterStore& params,
            const userver::formats::json::Value& JSON
    )
    {
        const auto& op = JSON["operator"].As<std::string>();
        if (!kCorrectOperators_.count(op)) {
            return ErrorDescriprion::kInvalidOperator;
        }

        for (const auto& tag : JSON["value"]) {
            if (!kCorrectTags_.count(tag.As<std::string>())) {
                return ErrorDescriprion::kInvalidValueType;
            }
        }

        params.PushBack(JSON["value"].As< std::vector<std::string> >());
        return fmt::format(
                kCorrectOperators_.at(op),
                kFieldName_,
                params.Size()
        );
    }


} // namespace service