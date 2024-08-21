#include "time_parser.hpp"
#include <sstream>
#include <iomanip>

namespace service {

std::string TimeParser::Parse(
    const userver::utils::datetime::TimeOfDay<std::chrono::seconds>& time
) {
    std::string time_str;
    time_str += std::to_string(time.Hours().count());
    time_str.push_back(':');
    time_str += std::to_string(time.Minutes().count());
    time_str.push_back(':');
    time_str += std::to_string(time.Seconds().count());
    return time_str;
}

    std::string TimeParser::ParseDate(
        std::chrono::system_clock::time_point time_point
    ){
        std::time_t time_t_value = std::chrono::system_clock::to_time_t(time_point);
        std::tm tm_value = *std::localtime(&time_t_value);
        std::stringstream ss;
        ss << std::put_time(&tm_value, "%Y-%m-%dT%H:%M:%S");
        ss << std::put_time(&tm_value, "%z");
        return ss.str();
    }


} // namespace service