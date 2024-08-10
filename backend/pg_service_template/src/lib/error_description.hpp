#pragma once

#include <unordered_map>
#include <string>

enum class ErrorDescriprion {
    kLowerLeftCornerNotSpecified,
    kTopRightCornerNotSpecified,
    kCornersNotSpecified,
    kTokenNotSpecified,
    kRestaurantNotFound,
};

extern const std::unordered_map<ErrorDescriprion, std::string> errorMapping;
