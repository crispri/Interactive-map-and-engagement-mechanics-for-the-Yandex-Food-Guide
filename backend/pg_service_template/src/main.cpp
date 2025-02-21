#include <userver/clients/http/component.hpp>
#include <userver/storages/postgres/component.hpp>
#include <userver/components/component.hpp>
#include <userver/clients/dns/component.hpp>
#include <userver/components/minimal_server_component_list.hpp>
#include <userver/server/handlers/ping.hpp>
#include <userver/server/handlers/tests_control.hpp>
#include <userver/testsuite/testsuite_support.hpp>
#include <userver/utils/daemon_run.hpp>

#include <handlers/guide/v1/ml/ml_sort.hpp>
#include <handlers/guide/v1/ml/ml_rate.hpp>
#include <handlers/guide/v1/restaurants/restaurants.hpp>
#include <handlers/guide/v1/restaurant_by_id/restaurant_by_id.hpp>
#include <handlers/guide/v1/selections/selections.hpp>
#include <handlers/guide/v1/selection_by_id/recommendations_by_selection_id.hpp>
#include <service/RestaurantService.hpp>
#include <service/SelectionService.hpp>
#include <service/MLService.hpp>
#include <handlers/guide/v1/collection-create/collection-create.hpp>
#include <handlers/guide/v1/insert-into-collection/insert-into-collection.hpp>
#include <handlers/guide/v1/login/login.hpp>
#include "handlers/guide/v1/insert-into-collection/insert-into-collection.hpp"
#include <service/SessionService.hpp>
#include "handlers/guide/v1/auth/auth_middleware.hpp"



int main(int argc, char* argv[]) {
  auto component_list = userver::components::MinimalServerComponentList()
                            .Append<userver::server::handlers::Ping>()
                            .Append<userver::components::TestsuiteSupport>()
                            .Append<userver::components::HttpClient>()
                            .Append<userver::server::handlers::TestsControl>()
                            .Append<userver::components::Postgres>("postgres-db-1")
                            .Append<userver::clients::dns::Component>();


  service::AppendRestaurantController(component_list);
  service::AppendRestaurantByIdController(component_list);
  service::AppendMLSort(component_list);
  service:: AppendSelections(component_list);
  service::AppendReccomendationsBySelectionId(component_list);


  service::AppendRestaurantService(component_list);
  service::AppendSelectionService(component_list);
  service::AppendMLService(component_list);
  service::AppendMLRate(component_list);
  service::AppendCollectionCreate(component_list);
  service::AppendInsertIntoCollection(component_list);

  service::AppendSessionController(component_list);
  service::AppendSessionService(component_list);
  service::AppendLogin(component_list);

  return userver::utils::DaemonMain(argc, argv, component_list);
}
