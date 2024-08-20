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

const std::unordered_map<std::string, size_t > PgSelectionRepository::default_user_collection_order = {
        {"Хочу сходить", 0},
        {"Хочу заказать", 1},
};

std::vector<TSelection> PgSelectionRepository::GetAll(const boost::uuids::uuid& user_id, bool return_collections) {

    if (return_collections) {

        const auto& selections = pg_cluster_->Execute(
            userver::storages::postgres::ClusterHostType::kSlave,
            R"(SELECT * FROM )" + kSelectionsTableName_ +
            R"( WHERE owner_id = $1; )",
            user_id
        );
        auto selections_vct = selections.AsContainer<std::vector<TSelection>>(userver::storages::postgres::kRowTag);
        std::vector<TSelection> selections_with_priority_vct(selections_vct.size());

        size_t i = default_user_collection_order.size();
        for (auto& selection : selections_vct) {
            /*
             *
             * Вот тут в теории может быть пиздец,
             * если добавить в БД новую коллекцию,
             * но не поддержать ее в мапе
             */
            if (selection.pre_created_collection_name) {
                selections_with_priority_vct[default_user_collection_order.at(selection.pre_created_collection_name.value())] =
                        std::move(selection);
            } else {
                selections_with_priority_vct[i++] = std::move(selection);
            }
        }

        return selections_with_priority_vct;
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
    int initial_size = 0;
    const auto& result = pg_cluster_->Execute(
        userver::storages::postgres::ClusterHostType::kSlave,
        R"( INSERT INTO guide.selections(name, description, owner_id, size) )"
        R"( VALUES ($1, $2, $3, $4) )"
        R"( RETURNING id; )",
        name,
        description,
        user_id,
        initial_size
    );
    return result[0].As< boost::uuids::uuid >();
}


void PgSelectionRepository::InsertIntoCollection(const boost::uuids::uuid& user_id, const boost::uuids::uuid& collection_id, const boost::uuids::uuid& restaurant_id) {
    pg_cluster_->Execute(
        userver::storages::postgres::ClusterHostType::kSlave,
        R"( INSERT INTO guide.places_selections(place_id, selection_id) )"
        R"( VALUES ($1, $2); )",
        restaurant_id, collection_id
    );

    const auto& prev_size = pg_cluster_->Execute(
        userver::storages::postgres::ClusterHostType::kSlave,
        R"( SELECT size FROM guide.selections )"
        R"( WHERE id = $1; )",
        collection_id
    );

    int size_after_insertion = prev_size[0].As< int >() + 1;

    pg_cluster_->Execute(
        userver::storages::postgres::ClusterHostType::kSlave,
        R"( UPDATE guide.selections )"
        R"( SET size = $1 )"
        R"( WHERE id = $2; )",
        size_after_insertion,
        collection_id
    );

    const auto& query_result = pg_cluster_->Execute(
        userver::storages::postgres::ClusterHostType::kSlave,
        R"( SELECT interior FROM guide.places )"
        R"( WHERE id = $1; )",
        restaurant_id
    );

    const auto& interiors_url = query_result.AsContainer<std::vector<std::string>>();
    const auto& interior = interiors_url[0];
    pg_cluster_->Execute(
        userver::storages::postgres::ClusterHostType::kSlave,
        R"( UPDATE guide.selections )"
        R"( SET picture = $1 )"
        R"( WHERE id = $2; )",
        interior,
        collection_id
    );
}


} // service