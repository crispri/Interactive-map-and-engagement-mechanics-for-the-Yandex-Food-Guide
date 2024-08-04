import './SheetContent.css'
import RestaurantCard from '../card/RestaurantCard.jsx'

const SheetContent = ({cardInfos}) => {
  return (
    <>
      {cardInfos.map((cardInfo, index) => (
            <RestaurantCard cardInfo={cardInfo}/>
      ))
      }
    </>
  )
}

export default SheetContent