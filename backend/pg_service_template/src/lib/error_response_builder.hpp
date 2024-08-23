#pragma once

#include "error_description.hpp"
#include <string>
#include <userver/server/http/http_request.hpp>

namespace service {


class ErrorResponseBuilder {

private:
    const userver::server::http::HttpRequest &request;

public:
    explicit ErrorResponseBuilder(
        const userver::server::http::HttpRequest &request
    );

    std::string build(
        userver::server::http::HttpStatus status,
        ErrorDescriprion error
    );

};


}; // namespace service