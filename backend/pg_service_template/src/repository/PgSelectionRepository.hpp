#pragma once

#include <userver/storages/postgres/cluster.hpp>

#include "ISelectionRepository.hpp"

namespace service {


class PgSelectionRepository : public ISelectionRepository {
    userver::storages::postgres::ClusterPtr pg_cluster_;
    const std::string kRestaurantsTableName_;
    const std::string kSelectionsTableName_;
    const std::string kConnectionTableName_;

    static const std::unordered_map<std::string, size_t> default_user_collection_order;

public:
    explicit PgSelectionRepository(const userver::storages::postgres::ClusterPtr& pg_cluster);

    std::vector<TSelection> GetAll(const boost::uuids::uuid& user_id, bool return_collections) override;
    std::vector<TRestaurant> GetById(const boost::uuids::uuid& selection_id, const boost::uuids::uuid& user_id) override;
    boost::uuids::uuid CreateCollection(const boost::uuids::uuid& user_id, const std::string& name, const std::string& description) override;
    void InsertIntoCollection(const boost::uuids::uuid& user_id, const boost::uuids::uuid& collection_id, const boost::uuids::uuid& restaurant_id) override;
};



} // service
