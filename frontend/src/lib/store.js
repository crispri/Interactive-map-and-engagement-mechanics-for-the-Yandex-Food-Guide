import { combineReducers, configureStore } from '@reduxjs/toolkit'
import restaurantsSlice from './restaurantsSlice'

const rootReducer = combineReducers({
  restaurantsSlice
})

export const store = configureStore({
  reducer: rootReducer
})
