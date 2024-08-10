#include "PgSelectionRepository.hpp"
#include "models/restaurant.hpp"

namespace service {


PgSelectionRepository::PgSelectionRepository(const userver::storages::postgres::ClusterPtr& pg_cluster) :
    pg_cluster_(pg_cluster),
    kRestaurantsTableName_("guide.places"),
    kSelectionsTableName_("guide.selections"),
    kConnectionTableName_("guide.places_selections")
    {}

std::vector<TSelection> PgSelectionRepository::GetAll() {
    const auto& selections = pg_cluster_->Execute(
        userver::storages::postgres::ClusterHostType::kSlave,
        R"(SELECT * FROM )" + kSelectionsTableName_ + ';'
    );
    return selections.AsContainer<std::vector<TSelection>>(userver::storages::postgres::kRowTag);
}
std::vector<TRestaurant> PgSelectionRepository::GetById(const boost::uuids::uuid& id) {
    const auto& restaurants = pg_cluster_->Execute(
        userver::storages::postgres::ClusterHostType::kSlave,
        R"(SELECT r.* FROM )" + kRestaurantsTableName_ + R"( r )"+
        R"(JOIN )" + kConnectionTableName_ + R"( cr ON r.id = cr.place_id WHERE cr.selection_id = $1;)",
        id
    );
    
   return restaurants.AsContainer<std::vector<TRestaurant>>(userver::storages::postgres::kRowTag);
}




} // service