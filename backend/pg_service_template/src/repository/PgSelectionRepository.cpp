#include "PgSelectionRepository.hpp"
#include <models/TRestaurant.hpp>
#include <string>
#include <userver/storages/postgres/cluster_types.hpp>

namespace service {


PgSelectionRepository::PgSelectionRepository(const userver::storages::postgres::ClusterPtr& pg_cluster) :
    pg_cluster_(pg_cluster),
    kRestaurantsTableName_("guide.places"),
    kSelectionsTableName_("guide.selections"),
    kConnectionTableName_("guide.places_selections")
    {}

std::vector<TSelection> PgSelectionRepository::GetAll(const boost::uuids::uuid& user_id, bool return_collections) {

    if (return_collections) {
        const auto& selections = pg_cluster_->Execute(
            userver::storages::postgres::ClusterHostType::kSlave,
            R"(SELECT * FROM )" + kSelectionsTableName_ +
            R"( WHERE owner_id = $1; )",
            user_id
        );
        return selections.AsContainer<std::vector<TSelection>>(userver::storages::postgres::kRowTag);
    }

    const auto& selections = pg_cluster_->Execute(
        userver::storages::postgres::ClusterHostType::kSlave,
        R"(SELECT * FROM )" + kSelectionsTableName_ +
        R"( WHERE owner_id IS NULL; )"
    );
    return selections.AsContainer<std::vector<TSelection>>(userver::storages::postgres::kRowTag);
    
}
std::vector<TRestaurant> PgSelectionRepository::GetById(const boost::uuids::uuid& selection_id, const boost::uuids::uuid& user_id) {
    const auto& restaurants = pg_cluster_->Execute(
        userver::storages::postgres::ClusterHostType::kSlave,
        R"(SELECT r.* FROM )" + kRestaurantsTableName_ + R"( r )"+
        R"(JOIN )" + kConnectionTableName_ + R"( cr ON r.id = cr.place_id WHERE cr.selection_id = $1;)",
        selection_id
    );   
   return restaurants.AsContainer<std::vector<TRestaurant>>(userver::storages::postgres::kRowTag);
}

boost::uuids::uuid PgSelectionRepository::CreateCollection(const boost::uuids::uuid& user_id, const std::string& name, const std::string& description) {
    const auto& result = pg_cluster_->Execute(
        userver::storages::postgres::ClusterHostType::kSlave,
        R"( INSERT INTO guide.selections(name, description, owner_id) )"
        R"( VALUES ($1, $2, $3) )"
        R"( RETURNING id; )",
        name,
        description,
        user_id
    );
    return result[0].As< boost::uuids::uuid >();
}


void PgSelectionRepository::InsertIntoCollection(const boost::uuids::uuid& user_id, const boost::uuids::uuid& collection_id, const boost::uuids::uuid& restaurant_id) {
    pg_cluster_->Execute(
        userver::storages::postgres::ClusterHostType::kSlave,
        R"(  )"
    );
}


} // service