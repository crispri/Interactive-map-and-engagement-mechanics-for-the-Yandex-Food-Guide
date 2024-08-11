import './SheetContent.css'
import RestaurantCard from '../card/RestaurantCard.jsx'

const SheetContent = ({cardInfos, setId, sheetRef}) => {
  return (
    <>
      {cardInfos.map((cardInfo, index) => (
        <div key={index}>
          <RestaurantCard cardInfo={cardInfo} setId={setId} sheetRef={sheetRef}/>
        </div>
      ))
      }
    </>
  )
}

export default SheetContent