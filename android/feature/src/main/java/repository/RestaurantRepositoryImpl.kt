package repository

import Utils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import model.Recommendation
import model.Restaurant


class RestaurantRepositoryImpl() : RestaurantRepository {

    override val recommendations: Flow<List<Recommendation>>
        get() = flowOf(Utils.recommendations)

    override val restaurants: Flow<List<Restaurant>>
        get() = flowOf(Utils.restaurants)

}