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

export const getSelections = createAsyncThunk(
	'restaurants/getSelections',
	async () => {
		try {
			const response = await fetch(
				`${_apiUrl}/guide/v1/selections`,
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

export const getSelection = createAsyncThunk(
	'restaurants/getSelection',
	async (selectionId) => {
		try {
			const response = await fetch(
				`${_apiUrl}/guide/v1/selections/${selectionId}`,
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

const restaurantsSlice = createSlice({
	name: 'restaurants',
	initialState: {
		mock_restaurants: [
			{
			  id: "1",
			  coordinates: {
				lat: 55.755826,
				lon: 37.617299,
			  },
			  name: "Русская кухня",
			  description: "Традиционная русская кухня с современным акцентом.",
			  address: "ул. Арбат, д. 12, Москва, Россия",
			  is_approved: true,
			  rating: 4.7,
			  price_lower_bound: 20,
			  price_upper_bound: 50,
			  tags: ["Русская", "Традиционная", "Современная"],
			  in_collection: true,
			},
			{
			  id: "2",
			  coordinates: {
				lat: 55.770717,
				lon: 37.621516,
			  },
			  name: "Грузинская Хинкальная",
			  description: "Настоящие грузинские хинкали и другие национальные блюда.",
			  address: "ул. Тверская, д. 25, Москва, Россия",
			  is_approved: true,
			  rating: 4.6,
			  price_lower_bound: 15,
			  price_upper_bound: 35,
			  tags: ["Грузинская", "Хинкали", "Национальная"],
			  in_collection: false,
			},
			{
			  id: "3",
			  coordinates: {
				lat: 55.757026,
				lon: 37.610514,
			  },
			  name: "Итальянская пицца",
			  description: "Настоящая итальянская пицца с хрустящей корочкой.",
			  address: "ул. Большая Никитская, д. 14, Москва, Россия",
			  is_approved: true,
			  rating: 4.8,
			  price_lower_bound: 25,
			  price_upper_bound: 60,
			  tags: ["Итальянская", "Пицца", "Фастфуд"],
			  in_collection: true,
			},
			{
			  id: "4",
			  coordinates: {
				lat: 55.743836,
				lon: 37.605015,
			  },
			  name: "Французское Бистро",
			  description: "Уютное французское бистро с отличным кофе и круассанами.",
			  address: "ул. Пятницкая, д. 10, Москва, Россия",
			  is_approved: true,
			  rating: 4.9,
			  price_lower_bound: 30,
			  price_upper_bound: 70,
			  tags: ["Французская", "Бистро", "Кофе"],
			  in_collection: true,
			},
			{
			  id: "5",
			  coordinates: {
				lat: 55.751244,
				lon: 37.618423,
			  },
			  name: "Японский Суши-Бар",
			  description: "Свежие суши и сашими прямо из Японии.",
			  address: "ул. Моховая, д. 15, Москва, Россия",
			  is_approved: true,
			  rating: 4.7,
			  price_lower_bound: 40,
			  price_upper_bound: 90,
			  tags: ["Японская", "Суши", "Морепродукты"],
			  in_collection: false,
			}
		  ],
		restaurants: [],
		unfocused_restaurants: {},
		current_pin: null,
		selections: []
	},
	reducers: {
		setCurrentPin: (state, action) => {
			state.current_pin = action.payload
		}
	},
	extraReducers: (builder) => {
		builder
			.addCase(getRestaurants.fulfilled, (state, action) => {
				state.restaurants = action.payload.items.map(el => {
					return ({
					  ...el,
					  coordinates: [el.coordinates.lon, el.coordinates.lat],
					})
				  })
				// state.restaurants.forEach(el => {
				// 	state.unfocused_restaurants[el.id] = false
				// })
			})
			.addCase(getSelections.fulfilled, (state, action) => {
				state.selections = action.payload.items
			})
	}
})

export const { setCurrentPin } = restaurantsSlice.actions
export default restaurantsSlice.reducer
