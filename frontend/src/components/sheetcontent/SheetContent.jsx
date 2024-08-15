import './SheetContent.css'
import RestaurantCard from '../card/RestaurantCard.jsx'
import { useSelector } from 'react-redux'
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
    <div style={{display: 'flex', flexDirection: 'column', gap: '16px'}}>
      {restaurants.map((restaurantInfo) => (
        <div key={restaurantInfo.id}>
          <RestaurantCard restaurantInfo={restaurantInfo}/>
        </div>
      ))
      }
    </div>
  )
}

export default SheetContent