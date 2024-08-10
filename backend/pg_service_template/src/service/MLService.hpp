#pragma once

#include <userver/components/component_base.hpp>
#include <userver/components/component_list.hpp>

namespace service {

class MLService final : public userver::components::ComponentBase {
 public:
  static constexpr std::string_view kName = "ml-service-component";

  MLService(const userver::components::ComponentConfig& config,
            const userver::components::ComponentContext& context);

  static void MLSort(std::vector<int>& restaurant_ids);
  //std::vector<std::pair<int, int>> SetRating(std::vector<int>& restaurant_ids);
};

void AppendMLService(
    userver::components::ComponentList& component_list);

}  // namespace service