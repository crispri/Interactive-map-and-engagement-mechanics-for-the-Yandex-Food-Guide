#include "time_parser.hpp"

namespace service {

std::string TimeParser::Parse(
    const userver::utils::datetime::TimeOfDay<std::chrono::seconds>& time) {
  std::string time_str;
  time_str += std::to_string(time.Hours().count());
  time_str.push_back(':');
  time_str += std::to_string(time.Minutes().count());
  time_str.push_back(':');
  time_str += std::to_string(time.Seconds().count());
  return time_str;
}

} // namespace service