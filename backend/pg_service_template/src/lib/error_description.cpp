#include "error_description.hpp"

#include <unordered_map>
#include <string>

const std::unordered_map<ErrorDescriprion, std::string> errorMapping = {
    {ErrorDescriprion::kCornersNotSpecified, "CORNERS_NOT_SPECIFIED"},
    {ErrorDescriprion::kLowerLeftCornerNotSpecified, "LOWER_LEFT_CORNER_NOT_SPECIFIED"},
    {ErrorDescriprion::kTopRightCornerNotSpecified, "TOP_RIGHT_CORNER_NOT_SPECIFIED"},
    {ErrorDescriprion::kTokenNotSpecified, "TOKEN_NOT_SPECIFIED"},
    {ErrorDescriprion::kInvalidSelectionId, "INVALID_SELECTION_ID"},
    {ErrorDescriprion::kListNotSpecified, "LIST_NOT_SPECIFIED"},
    {ErrorDescriprion::kRestaurantNotFound, "RESTAURANT_NOT_FOUND"},
    {ErrorDescriprion::kOperatorNotSpecified, "OPERATOR_NOT_SPECIFIED"},
    {ErrorDescriprion::kValueNotSpecified, "VALUE_NOT_SPECIFIED"},
    {ErrorDescriprion::kInvalidValueArraySize, "INVALID_VALUE_ARRAY_SIZE"},
    {ErrorDescriprion::kValueIsNotArray, "VALUE_IS_NOT_ARRAY"},
    {ErrorDescriprion::kInvalidOperator, "INVALID_OPERATOR"},
    {ErrorDescriprion::kPropertyNotSpecified, "PROPERTY_NOT_SPECIFIED"},
    {ErrorDescriprion::kInvalidPropertyName, "INVALID_PROPERTY_NAME"},
    {ErrorDescriprion::kInvalidValueType, "INVALID_VALUE_TYPE"},
    {ErrorDescriprion::kFiltersIsNotArray, "FILTERS_IS_NOT_ARRAY"},
    {ErrorDescriprion::kReturnCollectionsNotSpecified, "RETURN_COLLECTIONS_NOT_SPECIFIED"},
    {ErrorDescriprion::kOnlyCollectionsNotSpecified, "ONLY_COLLECTIONS_NOT_SPECIFIED"},
    {ErrorDescriprion::kInvalidOnlyCollectionsValue, "INVALID_ONLY_COLLECTIONS_VALUE"},
    {ErrorDescriprion::kCollectionNameNotSpecified, "COLLECTION_NAME_NOT_SPECIFIED"},
    {ErrorDescriprion::kCollectionDescriptionNotSpecified, "COLLECTION_DESCRIPTION_NOT_SPECIFIED"},
    {ErrorDescriprion::kRestaurantIdNotSpecified, "RESTAURANT_ID_NOT_SPECIFIED"},
};
