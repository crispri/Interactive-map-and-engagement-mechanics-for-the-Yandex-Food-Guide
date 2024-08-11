#pragma once

#include <string>
#include <string_view>

#include <userver/components/component_list.hpp>

namespace service {

void AppendSelections(userver::components::ComponentList& component_list);

}  // namespace service
