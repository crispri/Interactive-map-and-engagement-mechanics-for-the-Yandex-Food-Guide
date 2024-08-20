import { createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import { _apiUrl } from '../assets/variables'

export const getRestaurantById = createAsyncThunk(
	'restaurants/getRestaurantById',
	async (id) => {
		try {
			const response = await fetch(
				`${_apiUrl}/guide/v1/restaurants/${id}`,
				{
					method: "GET",
					headers: {
					  "Content-Type": "application/json;charset=utf-8",
					  "Authorization": "5142cece-b22e-4a4f-adf9-990949d053ff",
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
	name: 'restaurant',
	initialState: {
		restaurant: {},
	},
	reducers: {
	},
	extraReducers: (builder) => {
		builder
			.addCase(getRestaurantById.fulfilled, (state, action) => {
				state.restaurant = action.payload
				console.log(action.payload);
			})
	}
})

export default restaurantByIdSlice.reducer
