import { useDispatch } from 'react-redux';
import styles from './Filter.module.scss'
import { useEffect } from 'react';
import { getRestaurants } from '../../lib/restaurantsSlice.js'
import { setFilter, removeFilter, clearFilters } from '../../lib/filtersSlice.js';

const Filter = ({ filtersMap, debouncedValue, filter }) => {

    const dispatch = useDispatch();

    function handleClick() {
        dispatch(setFilter({ key: 'category', value: 'restaurant' })).then(() => {
            dispatch(getRestaurants({
                "lower_left_corner": {
                "lat": debouncedValue[1][1],
                "lon": debouncedValue[0][0]
                },
                "top_right_corner": {
                "lat": debouncedValue[0][1],
                "lon": debouncedValue[1][0]
                },
                "max_count": 0
            }))
        });   
    }

    return (
        <>
            <div onClick={handleClick}>
                Name: {filter.name}, Operator: {filter.operator}
            </div>           
        </>
    )
}

export default Filter;