#include <cstddef>
#include <string>
#include "IRestaurantFilterJSON.hpp"
#include "lib/error_description.hpp"

#include <unordered_map>

namespace service {

    class SelectionRestaurantFilterJSON : public IRestaurantFilterJSON {
    private:
        static const std::string kFieldName_;
        static const std::unordered_map<std::string, std::string> kCorrectOperators_;

    public:
        std::variant<std::string, ErrorDescriprion> BuildSQLFilter(
                userver::storages::postgres::ParameterStore&,
                const userver::formats::json::Value&
        ) override;
    };

} // namespace service