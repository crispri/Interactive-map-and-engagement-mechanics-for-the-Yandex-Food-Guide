#include "SelectionRestaurantFilterJSON.hpp"

#include <string>
#include <regex>

#include <lib/error_description.hpp>

#include <userver/utils/time_of_day.hpp>
#include <userver/formats/parse/common_containers.hpp>


namespace service {


    const std::string SelectionRestaurantFilterJSON::kFieldName_ = "id";
    const std::unordered_map<std::string, std::string> SelectionRestaurantFilterJSON::kCorrectOperators_ = {
            {"in", "IN"},
            {"not in", "NOT IN"},
    };

    std::variant<std::string, ErrorDescriprion> SelectionRestaurantFilterJSON::BuildSQLFilter(
            userver::storages::postgres::ParameterStore& params,
            const userver::formats::json::Value& JSON
    )
    {
        const auto& op = JSON["operator"].As<std::string>();
        if (!kCorrectOperators_.count(op)) {
            return ErrorDescriprion::kInvalidOperator;
        }
        const std::regex uuid_regex(R"(^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$)");
        for (const auto& uuid : JSON["value"]) {
            if (!std::regex_match(uuid.As<std::string>(), uuid_regex)) {
                return ErrorDescriprion::kInvalidValueType;
            }
        }

        params.PushBack(JSON["value"].As< std::vector<std::string> >());
        return fmt::format(
                " {} {} "
                "(SELECT place_id from guide.places_selections "
                "WHERE selection_id = ANY(${}::uuid[]))",
                kFieldName_,
                kCorrectOperators_.at(op),
                params.Size()
            );
    }


} // namespace service