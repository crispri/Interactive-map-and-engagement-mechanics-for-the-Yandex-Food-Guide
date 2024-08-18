import { useNavigate } from 'react-router-dom';
import './RestaurantCard.css'
import restaurantImage from '../../assets/restaurant_example.png'
import snippetFavourite from '../../assets/snippet_favourite.svg'
import snippetUnfavourite from '../../assets/snippet_unfavourite.svg'
import { useEffect } from 'react';
import { useDispatch } from 'react-redux';

const RestaurantCard = ({ restaurantInfo }) => {

  const navigateTo = useNavigate();

  function handleClick() {
    navigateTo("/restaurants/" + restaurantInfo?.id);
    // setId(restaurantInfo?.id);
    // sheetRef.current.snapTo(({ maxHeight }) => maxHeight);
  }

  function favouriteClick() {
    // useEffect
    console.log('favouriteClick')
    console.log(restaurantInfo?.pictures)
  }

  function unfavouriteClick() {
    console.log(restaurantInfo?.pictures)
    console.log('unfavouriteClick')
    // useEffect
  }

  return (
    <div className='card'>
      <div className='image-with-favourite'>
        {restaurantInfo?.in_collection
        ?
        <img className="fav_button" src={snippetFavourite} alt="Favourite" onClick={favouriteClick} />
        :
        <img className="fav_button" src={snippetUnfavourite} alt="Unfavourite" onClick={unfavouriteClick} />
        }
        {
          restaurantInfo?.pictures && restaurantInfo?.pictures?.length > 0
          ?
          <img className="snippet" src={restaurantInfo?.pictures[0]} alt="Restaurant" onClick={handleClick}/>
          :
          <img className="snippet" src={restaurantImage} alt="Restaurant" onClick={handleClick}/>
        }
      </div>
      <div className="content" onClick={handleClick}>
        <div className='title-rating'>
          <div className="title">{restaurantInfo?.name}</div>
          <div className="rating" onClick={handleClick}>
            <span className="star">★</span>
            <span>{Number(restaurantInfo?.rating).toFixed(1)}</span>
          </div>
        </div>
        <div className="info">
          <span className="info-item">До: {restaurantInfo?.close_time?.replace('0:0', '00')}</span>
          <span className="info-separator">·</span>
          <span className="info-item">{restaurantInfo?.address}</span>
        </div>
        <div className="description">{restaurantInfo?.description}</div>
        <div className="tags">
          {restaurantInfo?.tags?.map((tag, index) => (
            <div key={index} className="tag">{tag}</div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default RestaurantCard;