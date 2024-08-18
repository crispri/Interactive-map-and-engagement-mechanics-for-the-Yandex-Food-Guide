#pragma once

#include <string>
#include <userver/utils/time_of_day.hpp>

namespace service {

struct TimeParser {
    static std::string Parse(
        const userver::utils::datetime::TimeOfDay<std::chrono::seconds>& time
    );

    static std::string ParseDate(
       std::chrono::system_clock::time_point time_point
    );

};

} // namespace service