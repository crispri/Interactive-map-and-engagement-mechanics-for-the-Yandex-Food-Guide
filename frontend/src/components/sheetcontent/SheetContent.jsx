import './SheetContent.css'
import RestaurantCard from '../card/RestaurantCard.jsx'

const SheetContent = ({cardInfos}) => {
  return (
    <>
      {cardInfos.map((cardInfo, index) => (
        <div key={index}>
          <RestaurantCard cardInfo={cardInfo}/>
        </div>
      ))
      }
    </>
  )
}

export default SheetContent