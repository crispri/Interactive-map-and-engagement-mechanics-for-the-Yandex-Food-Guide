cmake_minimum_required(VERSION 3.12)
project(pg_service_template CXX)


# Adding userver dependency
find_package(userver COMPONENTS core postgresql QUIET)
if(NOT userver_FOUND)  # Fallback to subdirectory usage
    # Enable userver libraries that are needed in this project
    set(USERVER_FEATURE_POSTGRESQL ON CACHE BOOL "" FORCE)

    # Compatibility mode: some systems don't support these features
    set(USERVER_FEATURE_CRYPTOPP_BLAKE2 OFF CACHE BOOL "" FORCE)
    set(USERVER_FEATURE_GRPC_CHANNELZ OFF CACHE BOOL "" FORCE)
    set(USERVER_FEATURE_REDIS_HI_MALLOC ON CACHE BOOL "" FORCE)

    if (EXISTS ${CMAKE_CURRENT_SOURCE_DIR}/third_party/userver)
        message(STATUS "Using userver framework from third_party/userver")
        add_subdirectory(third_party/userver)
    else()
        message(FATAL_ERROR "Either install the userver or provide a path to it")
    endif()
endif()

userver_setup_environment()

include_directories("src/")

# Common sources
add_library(${PROJECT_NAME}_objs OBJECT
    src/handlers/guide/v1/restaurants/restaurants.hpp
    src/handlers/guide/v1/restaurants/restaurants.cpp
    src/handlers/guide/v1/ml/ml_sort.hpp
    src/handlers/guide/v1/ml/ml_sort.cpp
    src/handlers/guide/v1/ml/ml_rate.hpp
    src/handlers/guide/v1/ml/ml_rate.cpp
    src/handlers/guide/v1/restaurant_by_id/restaurant_by_id.cpp
    src/handlers/guide/v1/restaurant_by_id/restaurant_by_id.hpp
        src/models/TCoordinates.hpp
        src/models/TCoordinates.cpp
        src/models/TRestaurant.hpp
        src/models/TRestaurant.cpp
        src/models/TRestaurantFilter.hpp
        src/models/TRestaurantFilter.cpp
    src/lib/error_description.hpp
    src/lib/error_description.cpp
    src/lib/error_response_builder.hpp
    src/lib/error_response_builder.cpp
    src/handlers/guide/v1/selections/selections.hpp
    src/handlers/guide/v1/selections/selections.cpp
        src/models/TSelection.hpp
        src/models/TSelection.cpp
    src/handlers/guide/v1/selection_by_id/recommendations_by_selection_id.hpp
    src/handlers/guide/v1/selection_by_id/recommendations_by_selection_id.cpp

    src/lib/time_parser.hpp
    src/lib/time_parser.cpp
    src/repository/PgRestaurantRepository.hpp
    src/repository/PgRestaurantRepository.cpp
    src/repository/IRestaurantRepository.hpp
    src/repository/PgMLRepository.hpp
    src/repository/PgMLRepository.cpp
    src/repository/IMLRepository.hpp
    src/service/RestaurantService.hpp
    src/service/RestaurantService.cpp
    src/service/SelectionService.hpp
    src/service/SelectionService.cpp
    src/repository/PgSelectionRepository.hpp
    src/repository/PgSelectionRepository.cpp
    src/repository/ISelectionRepository.hpp
    src/handlers/guide/v1/selections/selections.hpp
    src/handlers/guide/v1/selections/selections.cpp
    src/handlers/guide/v1/selection_by_id/recommendations_by_selection_id.hpp
    src/handlers/guide/v1/selection_by_id/recommendations_by_selection_id.cpp    
    src/service/MLService.hpp
    src/service/MLService.cpp
        src/models/RestaurantFilterJSON/IRestaurantFilterJSON.hpp
        src/models/RestaurantFilterJSON/RatingRestaurantFilterJSON.cpp
        src/models/RestaurantFilterJSON/RatingRestaurantFilterJSON.hpp
        src/models/RestaurantFilterJSON/PriceLBRestaurantFilterJSON.hpp
        src/models/RestaurantFilterJSON/PriceUBRestaurantFilterJSON.hpp
        src/models/RestaurantFilterJSON/PriceUBRestaurantFilterJSON.cpp
        src/models/RestaurantFilterJSON/PriceLBRestaurantFilterJSON.cpp
        src/models/RestaurantFilterJSON/OpenTimeRestaurantFilterJSON.cpp
        src/models/RestaurantFilterJSON/OpenTimeRestaurantFilterJSON.hpp
        src/models/RestaurantFilterJSON/CloseTimeRestaurantFilterJSON.hpp
        src/models/RestaurantFilterJSON/CloseTimeRestaurantFilterJSON.cpp
        src/models/RestaurantFilterJSON/SelectionRestaurantFilterJSON.hpp
        src/models/RestaurantFilterJSON/SelectionRestaurantFilterJSON.cpp
        src/models/RestaurantFilterJSON/TagRestaurantFilterJSON.hpp
        src/models/RestaurantFilterJSON/TagRestaurantFilterJSON.cpp
        src/handlers/guide/v1/collection-create/collection-create.hpp
        src/handlers/guide/v1/collection-create/collection-create.cpp
        src/handlers/guide/v1/insert-into-collection/insert-into-collection.hpp
        src/handlers/guide/v1/insert-into-collection/insert-into-collection.cpp
    src/handlers/guide/v1/auth/auth_middleware.hpp
    src/handlers/guide/v1/auth/auth_middleware.cpp
    src/models/session.cpp
    src/models/session.hpp
    src/repository/PgSessionRepository.hpp
    src/repository/PgSessionRepository.cpp
    src/service/SessionService.hpp
    src/service/SessionService.cpp
        src/handlers/guide/v1/login/login.hpp
        src/handlers/guide/v1/login/login.cpp

)
target_link_libraries(${PROJECT_NAME}_objs PUBLIC userver::postgresql)


# The Service
add_executable(${PROJECT_NAME} src/main.cpp)
target_link_libraries(${PROJECT_NAME} PRIVATE ${PROJECT_NAME}_objs)


# Unit Tests
add_executable(${PROJECT_NAME}_unittest
    # src/hello_test.cpp
)
target_link_libraries(${PROJECT_NAME}_unittest PRIVATE ${PROJECT_NAME}_objs userver::utest)
add_google_tests(${PROJECT_NAME}_unittest)


# Benchmarks
add_executable(${PROJECT_NAME}_benchmark
    # src/hello_benchmark.cpp
)
target_link_libraries(${PROJECT_NAME}_benchmark PRIVATE ${PROJECT_NAME}_objs userver::ubench)
add_google_benchmark_tests(${PROJECT_NAME}_benchmark)


# Functional Tests
include(UserverTestsuite)

userver_testsuite_add_simple()


# Install
include(GNUInstallDirs)

if(DEFINED ENV{PREFIX})
  message(STATUS "Set install prefix: $ENV{PREFIX}")
  file(TO_CMAKE_PATH "$ENV{PREFIX}" PREFIX_PATH)
  set(CMAKE_INSTALL_PREFIX ${PREFIX_PATH})
endif()

file(GLOB CONFIGS_FILES ${CMAKE_CURRENT_SOURCE_DIR}/configs/*.yaml ${CMAKE_CURRENT_SOURCE_DIR}/configs/*.json)

install(TARGETS ${PROJECT_NAME} DESTINATION ${CMAKE_INSTALL_BINDIR} COMPONENT ${PROJECT_NAME})
install(FILES ${CONFIGS_FILES} DESTINATION ${CMAKE_INSTALL_SYSCONFDIR}/${PROJECT_NAME} COMPONENT ${PROJECT_NAME})
