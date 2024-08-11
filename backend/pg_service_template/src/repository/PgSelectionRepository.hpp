#pragma once

#include <userver/storages/postgres/cluster.hpp>

#include "ISelectionRepository.hpp"

namespace service {


class PgSelectionRepository : public ISelectionRepository {
    userver::storages::postgres::ClusterPtr pg_cluster_;
    const std::string kRestaurantsTableName_;
    const std::string kSelectionsTableName_;
    const std::string kConnectionTableName_;

public:
    explicit PgSelectionRepository(const userver::storages::postgres::ClusterPtr& pg_cluster);

    std::vector<TSelection> GetAll() override;
    std::vector<TRestaurant> GetById(const boost::uuids::uuid& id) override;
};



} // service
