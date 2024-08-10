#include "MLService.hpp"

#include <userver/components/component.hpp>
#include <userver/storages/postgres/component.hpp>

namespace service {

MLService::MLService(const userver::components::ComponentConfig& config,
                     const userver::components::ComponentContext& context)
    : userver::components::ComponentBase(config, context) {}

void MLService::MLSort(std::vector<int>& restaurant_ids) {
    std::random_device rd;
    std::mt19937 g(rd());

    std::shuffle(restaurant_ids.begin(), restaurant_ids.end(), g);
}

//int64_t GetHash(std::string_view &user_id, std::string_view &restaurant_id) {
//    std::string_view united = user_id + restaurant_id;
//    std::hash
//}
//
//std::vector<std::pair<int, int>> SetRating(std::vector<int>& restaurant_ids) {
//
//}

void AppendMLService(userver::components::ComponentList& component_list) {
    component_list.Append<MLService>();
}

}  // namespace service