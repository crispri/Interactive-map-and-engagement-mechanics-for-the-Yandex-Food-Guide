#include "error_response_builder.hpp"

#include <userver/formats/json/value_builder.hpp>
#include "error_description.hpp"

namespace service {

ErrorResponseBuilder::ErrorResponseBuilder(
    const userver::server::http::HttpRequest &request)
    : request(request) {}

std::string ErrorResponseBuilder::build(
    userver::server::http::HttpStatus status,
    ErrorDescriprion error
)
{
    userver::formats::json::ValueBuilder responseJSON;
    auto &response = request.GetHttpResponse();
    response.SetStatus(status);
    responseJSON["description"] = errorMapping.at(error);
    return userver::formats::json::ToPrettyString(
        responseJSON.ExtractValue(), 
        {' ', 4}
    );
}

}; // namespace service