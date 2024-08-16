import './SheetContent.css'
import {useEffect, useState} from 'react';
import useDebounce from '../../lib/useDebounce';
import RestaurantCard from '../card/RestaurantCard.jsx'
import { useInView } from "react-intersection-observer";
import { truncateString, formatTime } from '../../lib/utils.js'
import { setCurrentPin } from '../../lib/restaurantsSlice';
import { useDispatch } from 'react-redux';

const SheetContent = ({ restaurants }) => {
  
  restaurants = restaurants.map(el => ({
    id: el.id,
    name: el.name,
    description: truncateString(el.description, 200),
    address: el.address,
    is_approved: el.is_approved,
    rating: Number(el.rating).toFixed(2),
    price_lower_bound: el.price_lower_bound,
    price_upper_bound: el.price_upper_bound,
    tags: el.tags,
    close_time: formatTime(el.close_time),
    is_favorite: el.is_favourite
  }))
  // const dispatch = useDispatch()
  // const [currentRest, setCurrentRest] = useState(null);
  // const debouncedValue = useDebounce(currentRest, 300);
  

  // useEffect(() => {
  //   dispatch(setCurrentPin(debouncedValue))
  // }, [debouncedValue])

  return (
    <div style={{
      height: '100vh',
      overflowY: 'scroll',
      scrollSnapType: 'y mandatory',
      display: 'flex',
      flexDirection: 'column',
      gap: '16px'
    }}>
      {restaurants.map((restaurantInfo) => {
        const { ref, inView } = useInView({
          triggerOnce: false,
          threshold: 0.9,
        });
      
        if (inView) {
          console.log(`Restaurant in view: ${restaurantInfo.id}`);
          // setCurrentRest(restaurantInfo)
        }
      
      return (
        <div 
          key={restaurantInfo.id} 
          ref={ref}
          style={{
            scrollSnapAlign: 'start',
            height: '100vh',
            display: 'flex',
            alignItems: 'center', 
            justifyContent: 'center', 
          }}
        >
          <RestaurantCard restaurantInfo={restaurantInfo}/>
        </div>
      );
      })}
    </div>
  )
}

export default SheetContent