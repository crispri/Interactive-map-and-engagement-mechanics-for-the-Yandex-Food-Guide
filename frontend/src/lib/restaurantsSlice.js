import { createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import { _apiUrl } from '../assets/variables'

export const getRestaurants = createAsyncThunk(
	'restaurants/getRestaurants',
	async (coordinates) => {
		try {
			const response = await fetch(
				`${_apiUrl}/guide/v1/restaurants`,
				{
					method: "POST",
					headers: {
					  "Content-Type": "application/json;charset=utf-8",
					  "Authorization": "token",
					},
					body: JSON.stringify(coordinates),
				}
			)
			if (response.ok) {
				const data = await response.json();
				return data;
			} else {
			return response.status;
			}
		} catch (error) {
			return error
		}
	}
)

const restaurantsSlice = createSlice({
	name: 'restaurants',
	initialState: {
		restaurants: [],
	},
	reducers: {

	},
	extraReducers: (builder) => {
		builder
			.addCase(getRestaurants.fulfilled, (state, action) => {
				state.restaurants = action.payload
				console.log(action.payload);
			})
	}
})

export default restaurantsSlice.reducer