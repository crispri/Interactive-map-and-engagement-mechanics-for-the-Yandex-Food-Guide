#pragma once

#include <string>
#include <string_view>
#include <vector>

#include <userver/components/component_list.hpp>

namespace service {

void AppendMLSort(userver::components::ComponentList& component_list);

}  // namespace service
