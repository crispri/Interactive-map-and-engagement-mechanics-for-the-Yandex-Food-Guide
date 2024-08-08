#pragma once
#include <vector>
#include <string>

namespace service {

template <typename T>
struct IRepository {
    virtual std::vector<T> GetAll() = 0;
    virtual T GetById(const std::string&) = 0;
};

} // namespace service