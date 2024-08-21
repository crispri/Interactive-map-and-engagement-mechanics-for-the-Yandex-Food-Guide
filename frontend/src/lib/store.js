import { combineReducers, configureStore } from '@reduxjs/toolkit'
import restaurantsSlice from './restaurantsSlice'
import restaurantByIdSlice from './restaurantByIdSlice'
// import yandexMetricsSlice from './yandexMetricsSlice'

const rootReducer = combineReducers({
  restaurantsSlice,
  restaurantByIdSlice,
  // yandexMetricsSlice,
})

export const store = configureStore({
  reducer: rootReducer
})
