// import { createSlice } from '@reduxjs/toolkit';

// const filtersSlice = createSlice({
//     name: 'filters',
//     initialState: {
//         filters: new Map()
//     },
//     reducers: {
//       setFilter: (state, action) => {
//         const { key, value } = action.payload;
//         state.filters.set(key, value);
//       },
//       removeFilter: (state, action) => {
//         state.filters.delete(action.payload);
//       },
//       clearFilters: (state) => {
//         state.filters.clear();
//       },
//     },
//   });
  
//   export const { setFilter, removeFilter, clearFilters } = filtersSlice.actions;
  
//   export default filtersSlice.reducer;