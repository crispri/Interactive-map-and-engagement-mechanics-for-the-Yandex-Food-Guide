#pragma once

#include <string>
#include <string_view>
#include <vector>

#include <userver/components/component_list.hpp>

namespace service {

void AppendMLRate(userver::components::ComponentList& component_list);

}  // namespace service
