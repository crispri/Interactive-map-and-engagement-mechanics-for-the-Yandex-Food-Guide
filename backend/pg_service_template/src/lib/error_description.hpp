#pragma once

#include <unordered_map>
#include <string>

enum class ErrorDescriprion {
    kLowerLeftCornerNotSpecified,
    kTopRightCornerNotSpecified,
    kCornersNotSpecified,
    kTokenNotSpecified,
    kWrongIdSelection,
    kListNotSpecified,
    kRestaurantNotFound,
    kOperatorNotSpecified,
    kValueNotSpecified,
    kInvalidValueArraySize,
    kValueIsNotArray,
    kInvalidOperator,
};

extern const std::unordered_map<ErrorDescriprion, std::string> errorMapping;
