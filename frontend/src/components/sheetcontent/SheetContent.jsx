import './SheetContent.css'
import RestaurantCard from '../card/RestaurantCard.jsx'
import { useInView } from "react-intersection-observer";
import { truncateString, formatTime } from '../../lib/utils.js'

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