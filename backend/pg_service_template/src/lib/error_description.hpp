#pragma once

#include <unordered_map>
#include <string>

enum class ErrorDescriprion {
    kLowerLeftCornerNotSpecified,
    kTopRightCornerNotSpecified,
    kCornersNotSpecified,
    kTokenNotSpecified,
    kInvalidSelectionId,
    kListNotSpecified,
    kRestaurantNotFound,
    kOperatorNotSpecified,
    kValueNotSpecified,
    kInvalidValueArraySize,
    kValueIsNotArray,
    kInvalidOperator,
    kPropertyNotSpecified,
    kInvalidPropertyName,
    kInvalidValueType,
    kFiltersIsNotArray,
};

extern const std::unordered_map<ErrorDescriprion, std::string> errorMapping;
