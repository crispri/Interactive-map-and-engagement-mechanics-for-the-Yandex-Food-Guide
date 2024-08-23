#include <string>
#include "IRestaurantFilterJSON.hpp"
#include "lib/error_description.hpp"

#include <unordered_map>

namespace service {

    class TagRestaurantFilterJSON : public IRestaurantFilterJSON {
    private:
        static const std::string kFieldName_;
        static const std::unordered_map<std::string, std::string> kCorrectOperators_;
        static const std::unordered_set<std::string> kCorrectTags_;

    public:
        std::variant<std::string, ErrorDescriprion> BuildSQLFilter(
                userver::storages::postgres::ParameterStore&,
                const userver::formats::json::Value&
        ) override;
    };

} // namespace service