import { configureStore } from '@reduxjs/toolkit'
import restaurantsSlice from './restaurantsSlice'
import restaurantByIdSlice from './restaurantByIdSlice'

export const store = configureStore({
  reducer: {
	  restaurantsSlice,
    restaurantByIdSlice,
  },
})
