import { createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import { _yandexMetricsApiUrl } from '../assets/variables'

export const getRestaurantById = createAsyncThunk(
	'restaurants/getRestaurantById',
	async (id) => {
		try {
			const response = await fetch(
				`${_yandexMetricsApiUrl}/guide/v1/restaurants/${id}`,
				{
					method: "GET",
					headers: {
					  "Content-Type": "application/json;charset=utf-8",
					},
					credentials: 'include',
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
