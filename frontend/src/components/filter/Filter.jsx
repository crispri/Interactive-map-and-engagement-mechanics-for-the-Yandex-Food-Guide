import { useDispatch } from 'react-redux';
import styles from './Filter.module.scss'
import { useEffect } from 'react';
import { getRestaurants, addFilter, removeFilter } from '../../lib/restaurantsSlice.js';

const Filter = ({ filtersMap, debouncedValue, filter }) => {

    const dispatch = useDispatch();

    function addFilterHandler() {
        dispatch(addFilter({ key: filter.name, value: filter.operator }));   
    }

    function removeFilterHandler() {
        dispatch(removeFilter({ key: filter.name }));   
    }

    return (
        <>
            {
            filter.name in filtersMap && filtersMap[filter.name] != null && filtersMap ? 
            <div className={styles.inactive} onClick={removeFilterHandler}>
                {filter.name}
            </div>    
            :
            <div className={styles.active} onClick={addFilterHandler}>
            {filter.name}
        </div>       
            }
        </>
    )
}

export default Filter;