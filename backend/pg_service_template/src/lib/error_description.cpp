#include "error_description.hpp"

#include <unordered_map>
#include <string>

const std::unordered_map<ErrorDescriprion, std::string> errorMapping = {
    {ErrorDescriprion::kCornersNotSpecified, "CORNERS_NOT_SPECIFIED"},
    {ErrorDescriprion::kLowerLeftCornerNotSpecified, "LOWER_LEFT_CORNER_NOT_SPECIFIED"},
    {ErrorDescriprion::kTopRightCornerNotSpecified, "TOP_RIGHT_CORNER_NOT_SPECIFIED"},
    {ErrorDescriprion::kTokenNotSpecified, "TOKEN_NOT_SPECIFIED"},
    {ErrorDescriprion::kWrongIdSelection, "WRONG_ID_SELECTION"},
    {ErrorDescriprion::kListNotSpecified, "LIST_NOT_SPECIFIED"},
    {ErrorDescriprion::kRestaurantNotFound, "RESTAURANT_NOT_FOUND"},
    {ErrorDescriprion::kOperatorNotSpecified, "OPERATOR_NOT_SPECIFIED"},
    {ErrorDescriprion::kValueNotSpecified, "VALUE_NOT_SPECIFIED"},
    {ErrorDescriprion::kInvalidValueArraySize, "INVALID_VALUE_ARRAY_SIZE"},
    {ErrorDescriprion::kValueIsNotArray, "VALUE_IS_NOT_ARRAY"},
    {ErrorDescriprion::kInvalidOperator, "INVALID_OPERATOR"},

};
