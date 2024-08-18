import styles from './FiltersFullView.module.scss'
import { useEffect } from 'react';

const FiltersFullView = ({sheetRef}) => {

    useEffect(() => {
        sheetRef.current.snapTo(({ maxHeight }) => maxHeight);
        console.log('Full view of filters');
        // dispatch(getRestaurantById(restId))
        // .then((response) => {
        //   console.log('Restaurant data:', response);
        // })
        // .catch((error) => {
        //   console.error('Error fetching restaurant:', error);
        // });
      }, []);

    return (
        <>
            <h1>AAAAAAAAAAAAAAAAAAAAAa</h1>
        </>
    )
}


export default FiltersFullView;