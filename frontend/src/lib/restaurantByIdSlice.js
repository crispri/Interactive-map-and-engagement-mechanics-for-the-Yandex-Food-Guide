import { createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import { _apiUrl } from '../assets/variables'

export const getRestaurantById = createAsyncThunk(
	'restaurants/getRestaurantById',
	async (id) => {
		try {
			const response = await fetch(
				`${_apiUrl}/guide/v1/restaurants/7d406e96-e5c6-45fc-bddc-9de8eb8c0c10`,
				{
					method: "GET",
					headers: {
					  "Content-Type": "application/json;charset=utf-8",
					  "Authorization": "token",
					},
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

const restaurantByIdSlice = createSlice({
	name: 'restaurants',
	initialState: {
		restaurants: [],
	},
	reducers: {

	},
	extraReducers: (builder) => {
		builder
			.addCase(getRestaurantById.fulfilled, (state, action) => {
				state.restaurants = action.payload
				console.log(action.payload);
			})
	}
})

export default restaurantByIdSlice.reducer
