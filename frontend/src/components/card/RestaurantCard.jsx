import './RestaurantCard.css'
import restaurantImage from '../../assets/restaurant_example.png'
import snippetFavourite from '../../assets/snippet_favourite.svg'

const RestaurantCard = ({ cardInfo }) => {
  return (
    <div className='card'>
      <div className='image-with-favourite'>
        <img className="fav_button" src={snippetFavourite} alt="Favourite" onClick={() => { }} />
        <img className="snippet" src={restaurantImage} alt="Restaurant" />
      </div>
      <div className="content">
        <div className='title-rating'>
          <div className="title">{cardInfo.title}</div>
          <div className="rating">
            <span className="star">★</span>
            <span>4.8</span>
          </div>
        </div>
        <div className="info">
          <span className="info-item">{cardInfo.openUntil}</span>
          <span className="info-separator">·</span>
          <span className="info-item">Медведково</span>
          <span className="info-separator">·</span>
          <span className="info-item">9 мин на машине</span>
        </div>
        <div className="description">{cardInfo.description}</div>
        <div className="tags">
          {cardInfo.tags.map((tag, index) => (
            <div key={index} className="tag">{tag}</div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default RestaurantCard;