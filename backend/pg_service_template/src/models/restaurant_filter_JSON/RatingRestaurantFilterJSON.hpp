#include <cstddef>
#include <string>
#include "IRestaurantFilterJSON.hpp"
#include "lib/error_description.hpp"

#include <unordered_map>

namespace service {

class RatingRestaurantFilter : IRestaurantFilterJSON {
private:
    static constexpr int kValueArraySize_ = 1;
    static const std::string kFieldName_;
    static const std::unordered_map<std::string, std::string> kCorrectOperators_;
    const userver::formats::json::Value& kJSON_;
    
public:
    RatingRestaurantFilter(const userver::formats::json::Value& JSON);
    std::variant<std::string, ErrorDescriprion> BuildSQLFilter(std::size_t) override;
};

} // namespace service