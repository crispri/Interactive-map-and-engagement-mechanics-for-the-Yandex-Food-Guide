import './SheetContent.css'
import RestaurantCard from '../card/RestaurantCard.jsx'
import { useSelector } from 'react-redux'
import { truncateString, formatTime } from '../../lib/utils.js'
import { useParams } from 'react-router-dom'

const RestaurantSnippet = ({}) => {

    const {restId} = useParams()
    const restaurant = useSelector((state) => state.restaurantsSlice.restaurants).
        filter(el => el.id === restId).
        map(el => ({
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
            in_collection: el.in_collection
        }))
    return (
        <>
            <div key={index}>
                <RestaurantCard restaurantInfo={restaurant[0]}/>
            </div>
        </>
    )
}

export default RestaurantSnippet