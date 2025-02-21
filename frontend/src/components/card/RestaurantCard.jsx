import { useNavigate } from 'react-router-dom';
import './RestaurantCard.css'
import restaurantImage from '../../assets/restaurant_example.png'
import snippetFavourite from '../../assets/snippet_favourite.svg'
import snippetUnfavourite from '../../assets/snippet_unfavourite.svg'
import { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { getCollections } from '../../lib/restaurantsSlice';

const RestaurantCard = ({ setCurrentRest, collectionSetOpen, collectionRef, restaurantInfo }) => {

  const navigateTo = useNavigate();

  const dispatch = useDispatch();

  function handleClick() {
    ym(98116436,'reachGoal','open_on_full_screen_restaurant_card');
    navigateTo("/restaurants/" + restaurantInfo?.id);
    // setId(restaurantInfo?.id);
    // sheetRef.current.snapTo(({ maxHeight }) => maxHeight);
  }

  function favouriteClick() {
    // useEffect
    // console.log('favouriteClick')
    // console.log(restaurantInfo?.pictures)
    collectionSetOpen(true);
    setCurrentRest(restaurantInfo?.id)
    // collectionRef.current.snapTo(({ maxHeight }) => maxHeight);
  }

  function unfavouriteClick() {

    console.log(restaurantInfo?.pictures);
    console.log('unfavouriteClick');
    dispatch(getCollections())
    collectionSetOpen(true);


    console.log(collectionRef.current);
    // collectionRef.current.snapTo(({ maxHeight }) => maxHeight);
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