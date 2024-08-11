#pragma once

#include <boost/uuid/uuid_io.hpp>
#include <repository/PgMLRepository.hpp>
#include <userver/components/component_base.hpp>
#include <userver/components/component_list.hpp>

namespace service {

    class MLService final : public userver::components::ComponentBase {
        std::shared_ptr<IMLRepository> repository_;

    public:
        static constexpr std::string_view kName = "ml-service-component";

        MLService(const userver::components::ComponentConfig &config,
                  const userver::components::ComponentContext &context);

        static void MLSort(std::vector<int> &restaurant_ids);

        static std::vector<std::pair<int, int>> SetScore(
                boost::uuids::uuid &user_id, std::vector<int> &restaurant_ids);

        boost::uuids::uuid GetUserIdByAuthToken(const boost::uuids::uuid& session_id);

    private:
        static uint32_t GetHash(boost::uuids::uuid &user_id, int &restaurant_id);

    };

    void AppendMLService(userver::components::ComponentList &component_list);

}  // namespace service