import styles from './CollectionsList.module.scss'
import { useDispatch, useSelector } from 'react-redux'
import { setSelection } from '../../lib/restaurantsSlice'
import { useRef } from 'react'
import { getRestaurants } from '../../lib/restaurantsSlice'
import info_picture from '../../assets/info.svg'
import bookmark_outline from '../../assets/bookmark_outline.svg'
import wantToGo from '../../assets/want_to_go_map.svg'
import wantToOrder from '../../assets/want_to_order_map.svg'

function CollectionsList() {
	const selections = useSelector((state) => state.restaurantsSlice.collections)
	const current_selection = useSelector((state) => state.restaurantsSlice.currentSelection)
	const restaurants = useSelector((state) => state.restaurantsSlice.restaurants)
	const dispatch = useDispatch();
	const wrapperRef = useRef(null);

	function onSetSelection(id) {
		if (current_selection === id) {
			dispatch(setSelection(null))
		} else {
			dispatch(setSelection(id))
			ym(98116436,'reachGoal','map_main_screen_button_collection');
			setTimeout(() => {
				const selectedCard = document.querySelector(`.${styles.card_active}`);
				if (selectedCard && wrapperRef.current) {
					const cardRect = selectedCard.getBoundingClientRect();
					const wrapperRect = wrapperRef.current.getBoundingClientRect();
					const scrollLeft = wrapperRef.current.scrollLeft;
					const newScrollLeft = scrollLeft + (cardRect.left + cardRect.width / 2) - (wrapperRect.width / 2);
					wrapperRef.current.scrollTo({
						left: newScrollLeft,
						behavior: 'smooth'
					});
				}
			}, 300);
		}
	}

	const cards = selections?.map((el) => {
		return (<div 
			key={el.id} 
			className={`${styles.card} ${current_selection === el.id ? styles.card_active : ''}`} 
			onClick={() => onSetSelection(el.id)}
			// style={{width : current_selection === el.id ? '274px' : '146px'}}
			transition={{ duration: 0.3 }}
			
			>
				<div className={styles.fade}></div>
				{	el?.pre_created_collection_name === "Хочу сходить"
                    ?
                    <img src={wantToGo} alt={el.name} /> 
                    :
                    el?.pre_created_collection_name === "Хочу заказать"
                    ?
                    <img src={wantToOrder} alt={el.name} />
                    :
                    <img src={el.picture} alt={el.name} className={styles.picture}/>
                }
				{/* <img src={el.picture} alt="image" className={styles.picture}/> */}
				<div className={styles.text}>
					{current_selection === el.id 
					? <div style={{display: 'flex', justifyContent: 'space-between'}}>
						<img src={info_picture} alt="info" />
						<p>{restaurants.length} мест</p>
						<img src={bookmark_outline} alt="bookmark" />
					</div>
					: null}
					<p>{el.name}</p>
				</div>
		</div>)})
  return (
	<div className={styles.wrapper} ref={wrapperRef}>
		<div className={styles.categories}>
            {/* <div className={styles.card} transition={{ duration: 0.3 }}>

            </div>
            <div className={styles.card} transition={{ duration: 0.3 }}> */}

            {/* </div> */}
			{cards}
		</div>
	</div>
  )
}

export default CollectionsList
