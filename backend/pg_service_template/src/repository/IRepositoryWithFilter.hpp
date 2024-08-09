#pragma once
#include "IRepository.hpp"

namespace service {

template <typename T, typename U>
struct IRepositoryWithFilter : IRepository<T> {
    virtual std::vector<T> GetByFilter(const U&) = 0;
};

};