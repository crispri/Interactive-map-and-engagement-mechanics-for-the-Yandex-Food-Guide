import styles from './SelectionsList.module.scss'
import { useDispatch, useSelector } from 'react-redux'
import { setSelection } from '../../lib/restaurantsSlice'
import { useRef } from 'react'
import { getRestaurants } from '../../lib/restaurantsSlice'
import info_picture from '../../assets/info.svg'
import bookmark_outline from '../../assets/bookmark_outline.svg'

import ultima_wide from '../../assets/ultima_wide.png'
import experts_wide from '../../assets/experts_wide.png'
import ultima from '../../assets/ultima.png'
import experts from '../../assets/experts.png'

function SelectionsList() {
	const selections = useSelector((state) => state.restaurantsSlice.selections)
	const current_selection = useSelector((state) => state.restaurantsSlice.currentSelection)
	const restaurants = useSelector((state) => state.restaurantsSlice.restaurants)
	const dispatch = useDispatch();
	const wrapperRef = useRef(null);

	function onSetSelection(id) {
		if (current_selection === id) {
			dispatch(setSelection(null))
		} else {
			dispatch(setSelection(id))
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
				<img src={el.picture} alt="image" className={styles.picture}/>
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
	cards.unshift(
		<div 
			key={'ultima'} 
			className={`${styles.ultima} ${current_selection === 'ultima' ? styles.ultima_active : ''}`} 
			onClick={() => onSetSelection('ultima')}
			transition={{ duration: 0.3 }}
			>
				<img src={current_selection === 'ultima' ? ultima_wide : ultima} alt="image" className={styles.picture}/>
				<div className={styles.ultima__text} style={{color: 'black', alignSelf: current_selection === 'ultima' ? 'center' : 'flex-end'}}>
					<p style={{fontSize: current_selection === 'ultima' ? '13px' : '11px'}}>Топ-50 ресторанов Москвы</p>
				</div>
		</div>,
		<div 
			key={'experts'} 
			className={`${styles.ultima} ${current_selection === 'experts' ? styles.ultima_active : ''}`} 
			onClick={() => onSetSelection('experts')}
			transition={{ duration: 0.3 }}
			>
				<img src={current_selection === 'experts' ? experts_wide : experts} alt="image" className={styles.picture}/>
				<div className={styles.ultima__text} style={{color: 'black', alignSelf: 'center'}}>
					<p style={{fontSize: '9px'}}>Открытая кухня</p>
					<p style={{fontSize: '13px', fontWeight: '900'}}>Выбор экспертов</p>
				</div>
		</div>
	)
  return (
	<div className={styles.wrapper} ref={wrapperRef}>
		<div className={styles.categories}>
			{cards}
		</div>
	</div>
  )
}

export default SelectionsList
