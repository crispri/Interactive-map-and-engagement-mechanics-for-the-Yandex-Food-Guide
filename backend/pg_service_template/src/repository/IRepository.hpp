#pragma once
#include <vector>
#include <string>


/*
 * IRestaurantRepository
 *
 * std::vector<TRestaurant>
 *
 * + ходить в файлик не только для моков
 *
 *
 * 
 */

namespace service {

template <typename T>
struct IRepository {
    virtual std::vector<T> GetAll() = 0;
    virtual T GetById(const std::string&) = 0;
};

} // namespace service