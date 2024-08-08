import { configureStore } from '@reduxjs/toolkit'
import restaurantsSlice from './restaurantsSlice'

export const store = configureStore({
  reducer: {
	restaurantsSlice
  },
})
