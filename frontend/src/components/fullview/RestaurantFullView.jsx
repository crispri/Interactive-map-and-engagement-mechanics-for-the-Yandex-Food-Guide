import styles from './RestaurantFullView.module.scss'
import { useDispatch, useSelector } from 'react-redux'
import { useEffect } from 'react'
import { getRestaurantById } from '../../lib/restaurantByIdSlice.js'
import { useParams } from 'react-router-dom'
import { truncateString, formatTime } from '../../lib/utils.js'

function map(el) {
  return {
    id: el.id,
    name: el.name,
    description: truncateString(el.description, 200),
    address: el.address,
    is_approved: el.is_approved,
    rating: Number(el.rating).toFixed(2),
    price_lower_bound: el.price_lower_bound,
    price_upper_bound: el.price_upper_bound,
    tags: el.tags,
    // close_time: formatTime(el.close_time),
    in_collection: el.in_collection
  }
}

const RestaurantFullView = ({ sheetRef }) => {

    const dispatch = useDispatch();
    const {restId} = useParams()

    const restaurant = map(useSelector((state) => state.restaurantByIdSlice.restaurant))

    useEffect(() => {
        sheetRef.current.snapTo(({ maxHeight }) => maxHeight);
        console.log('Full view of restaurant');
        dispatch(getRestaurantById(restId))
        .then((response) => {
          console.log('Restaurant data:', response);
        })
        .catch((error) => {
          console.error('Error fetching restaurant:', error);
        });
      }, []);

      return (
        <div className={styles.container}>
          <div className={styles.header}>
            <button className={styles.backButton}>←</button>
            <div className={styles.title}>{restaurant.name}</div>
            <div className={styles.actions}>
              <button className={styles.shareButton}>Share</button>
            </div>
          </div>
          <div className={styles.rating}>
            <span className={styles.star}>★</span>
            <span className={styles.score}>{restaurant.rating}</span>
            <span className={styles.reviews}>82 оценки</span>
          </div>
          <div className={styles.images}>
            <img src="image1.jpg" alt="Restaurant Interior 1" className={styles.mainImage} />
            <div className={styles.sideImages}>
              <img src="image2.jpg" alt="Restaurant Interior 2" />
              <img src="image3.jpg" alt="Restaurant Interior 3" />
            </div>
          </div>
          <div className={styles.info}>
            <div className={styles.tags}>
              <span className={styles.tag}>Итальянская</span>
              <span className={styles.tag}>Европейская</span>
            </div>
            <p className={styles.description}>
              Коктейль-бар «Мэлт» — это стильное и уютное место, где вы можете насладиться вкусной едой и напитками. Здесь вы можете попробовать...
            </p>
          </div>
          <div className={styles.footer}>
            <button className={styles.actionButton}>Такси</button>
            <button className={styles.actionButton}>Маршрут</button>
            <button className={styles.actionButton}>Позвонить</button>
          </div>
        </div>
      );
}

export default RestaurantFullView;