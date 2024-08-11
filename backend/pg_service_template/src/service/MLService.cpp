#include "MLService.hpp"

#include <userver/components/component.hpp>
#include <userver/storages/postgres/component.hpp>
#include <boost/uuid/uuid_io.hpp>

namespace service {

MLService::MLService(const userver::components::ComponentConfig& config,
                     const userver::components::ComponentContext& context
):
      userver::components::ComponentBase(
          config,
          context
     ),
      repository_(
          std::make_shared<PgMLRepository>(
              context
                  .FindComponent<userver::components::Postgres>("postgres-db-1")
                  .GetCluster()
                  )
      )
{}

void MLService::MLSort(std::vector<int>& restaurant_ids) {
    std::random_device rd;
    std::mt19937 g(rd());

    std::shuffle(restaurant_ids.begin(), restaurant_ids.end(), g);
}

uint32_t MLService::GetHash(boost::uuids::uuid &user_id, int &restaurant_id) {
    std::string united = boost::uuids::to_string(user_id) + std::to_string(restaurant_id);
    return std::hash<std::string>{}(united) % 1000000;
}

std::vector<std::pair<int, int>> MLService::SetScore(
    boost::uuids::uuid &user_id, std::vector<int>& restaurant_ids) {
    std::vector<std::pair<int, int>> rated_ids;

    for (auto id : restaurant_ids) {
        rated_ids.emplace_back(id, MLService::GetHash(user_id, id));
    }

    return rated_ids;
}

boost::uuids::uuid MLService::GetUserIdByAuthToken(const boost::uuids::uuid& session_id) {
    return repository_->GetUserIdByAuthToken(session_id);
}

void AppendMLService(userver::components::ComponentList& component_list) {
    component_list.Append<MLService>();
}

}  // namespace service