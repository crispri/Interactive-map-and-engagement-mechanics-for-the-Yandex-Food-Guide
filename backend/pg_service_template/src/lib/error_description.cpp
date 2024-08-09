#include "error_description.hpp"
#include <asm-generic/errno-base.h>
#include <unordered_map>
#include <string>

const std::unordered_map<ErrorDescriprion, std::string> errorMapping = {
    {ErrorDescriprion::kCornersNotSpecified, "CORNERS_NOT_SPECIFIED"},
    {ErrorDescriprion::kLowerLeftCornerNotSpecified, "LOWER_LEFT_CORNER_NOT_SPECIFIED"},
    {ErrorDescriprion::kTopRightCornerNotSpecified, "TOP_RIGHT_CORNER_NOT_SPECIFIED"},
    {ErrorDescriprion::kTokenNotSpecified, "TOKEN_NOT_SPECIFIED"},
    {ErrorDescriprion::kListNotSpecified, "LIST_NOT_SPECIFIED"}
};
