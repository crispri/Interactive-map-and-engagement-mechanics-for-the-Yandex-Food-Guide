import { combineReducers, configureStore } from '@reduxjs/toolkit'
import restaurantsSlice from './restaurantsSlice'
import restaurantByIdSlice from './restaurantByIdSlice'
// import filtersSlice from './filtersSlice'

const rootReducer = combineReducers({
  restaurantsSlice,
  restaurantByIdSlice,
  // filtersSlice,
})

export const store = configureStore({
  reducer: rootReducer
})
