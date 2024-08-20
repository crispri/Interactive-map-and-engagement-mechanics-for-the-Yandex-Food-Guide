#pragma once

#include <vector>
#include <string>

#include <models/TSelection.hpp>
#include <models/TRestaurant.hpp>



namespace service {

struct ISelectionRepository  {
    virtual std::vector<TSelection> GetAll(const boost::uuids::uuid& user_id, bool return_collections) = 0;
    virtual std::vector<TRestaurant> GetById(const boost::uuids::uuid& selection_id, const boost::uuids::uuid& user_id) = 0;
    virtual ~ISelectionRepository() = default;
    virtual void InsertIntoCollection(const boost::uuids::uuid& user_id, const boost::uuids::uuid& collection_id, const boost::uuids::uuid& restaurant_id) = 0;
    virtual boost::uuids::uuid CreateCollection(const boost::uuids::uuid& user_id, const std::string& name, const std::string& description) = 0;
};


} // namespace service