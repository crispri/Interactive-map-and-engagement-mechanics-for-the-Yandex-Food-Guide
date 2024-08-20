import { combineReducers, configureStore } from '@reduxjs/toolkit'
import restaurantsSlice from './restaurantsSlice'
import restaurantByIdSlice from './restaurantByIdSlice'

const rootReducer = combineReducers({
  restaurantsSlice,
  restaurantByIdSlice,
})

export const store = configureStore({
  reducer: rootReducer
})
