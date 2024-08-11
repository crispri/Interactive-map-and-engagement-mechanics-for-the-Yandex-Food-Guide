#include "selections.hpp"
#include "../../../../lib/error_response_builder.hpp"
#include "../../../../models/TSelection.hpp"

#include <fmt/format.h>

#include <userver/components/component.hpp>
#include <userver/formats/json/serialize.hpp>
#include <userver/formats/json/value.hpp>
#include <userver/logging/log.hpp>
#include <userver/server/handlers/http_handler_base.hpp>
#include <userver/storages/postgres/cluster.hpp>
#include <userver/storages/postgres/component.hpp>
#include <userver/utils/assert.hpp>

namespace service {

namespace {

class Selections final : public userver::server::handlers::HttpHandlerBase {
 public:
  static constexpr std::string_view kName = "handler-selections";

    Selections(
        const userver::components::ComponentConfig& config,
        const userver::components::ComponentContext& component_context
    ) : 
    
    HttpHandlerBase(
        config,
        component_context
    ),
    pg_cluster_(
      component_context
        .FindComponent<userver::components::Postgres>("postgres-db-1")
        .GetCluster()
    )
    {}

    std::string HandleRequestThrow(
        const userver::server::http::HttpRequest& request,
        userver::server::request::RequestContext&
    ) const override 
    {
      ErrorResponseBuilder errorBuilder(request);

      if (!request.HasHeader("Authorization")) {
        return errorBuilder.build(
            userver::server::http::HttpStatus::kUnauthorized,
            ErrorDescriprion::kTokenNotSpecified
        );
      }
      
      TSelection selection;

      selection.id = 1;
      selection.description = "описание";
      selection.is_public = true;
      selection.name = "lady`s breakfasts";
    

      userver::formats::json::ValueBuilder responseJSON{selection};
      return userver::formats::json::ToPrettyString(responseJSON.ExtractValue(),
                                                  {' ', 4});

    }

  userver::storages::postgres::ClusterPtr pg_cluster_;
};

}  // namespace

void AppendSelections(userver::components::ComponentList& component_list) {
  component_list.Append<Selections>();
}

}  // namespace service
