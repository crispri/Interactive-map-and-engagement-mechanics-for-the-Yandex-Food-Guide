import './SheetContent.css'
import {useEffect, useState} from 'react';
import useDebounce from '../../lib/useDebounce';
import RestaurantCard from '../card/RestaurantCard.jsx'
import { useInView } from "react-intersection-observer";
import { truncateString, formatTime } from '../../lib/utils.js'
import { setCurrentPin } from '../../lib/restaurantsSlice';
import { useDispatch } from 'react-redux';
import { InView } from 'react-intersection-observer';

const SheetContent = ({ restaurants }) => {
  
  restaurants = restaurants.map(el => ({
    id: el.id,
    name: el.name,
    coordinates: el.coordinates,
    description: truncateString(el.description, 200),
    address: el.address,
    is_approved: el.is_approved,
    rating: Number(el.rating).toFixed(2),
    price_lower_bound: el.price_lower_bound,
    price_upper_bound: el.price_upper_bound,
    tags: el.tags,
    close_time: formatTime(el.close_time),
    in_collection: el.in_collection,
    pictures: el.pictures,
  }))
  const dispatch = useDispatch()
  const [currentRest, setCurrentRest] = useState(null);
  const debouncedValue = useDebounce(currentRest, 300);  

  useEffect(() => {
    dispatch(setCurrentPin(debouncedValue))
  }, [debouncedValue])

  return (
    <div style={{
      height: '100vh',
      overflowY: 'scroll',
      scrollSnapType: 'y mandatory',
      display: 'flex',
      flexDirection: 'column',
      gap: '16px'
    }}>
      {restaurants.map((el) => {
        return (
          <InView 
            onChange={(inView) => inView && setCurrentRest(el)}
            as='div'
            key={el?.id} 
            threshold={0.9}
          >
            {({ref}) => (
              <div
                ref={ref}
                style={{
                  scrollSnapAlign: 'start',
                }}
              >
                <RestaurantCard restaurantInfo={el}/>
              </div>
            )}
          </InView>
        )
      })}
    </div>
  )
}

export default SheetContent