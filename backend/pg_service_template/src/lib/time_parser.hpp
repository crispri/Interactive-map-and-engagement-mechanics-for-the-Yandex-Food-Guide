#pragma once

#include <string>
#include <userver/utils/time_of_day.hpp>

namespace service {

struct TimeParser {
  static std::string Parse(
      const userver::utils::datetime::TimeOfDay<std::chrono::seconds>& time
    );
};

} // namespace service