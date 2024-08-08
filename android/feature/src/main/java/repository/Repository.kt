package repository

import kotlinx.coroutines.flow.Flow
import model.Recommendation
import model.Restaurant

interface RestaurantRepository {

    val recommendations: Flow<List<Recommendation>>

    val restaurants: Flow<List<Restaurant>>


}

