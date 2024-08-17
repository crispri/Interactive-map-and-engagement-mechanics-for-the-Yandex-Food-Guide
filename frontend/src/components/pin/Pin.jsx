import React from 'react'
import icon from '../../assets/fork_knife.svg'
import styles from './Pin.module.scss'
import star_black from '../../assets/star_black.svg'
import bottom_center_white from '../../assets/BottomCenter_white.svg'
import bottom_center_black from '../../assets/BottomCenter_black.svg'
import bookmark_black from '../../assets/bookmark_black.svg'
import star_white from '../../assets/star_white.svg'
import bookmark_white from '../../assets/bookmark_white.svg'

function Pin({type, item, isFocused, onClick, outsideClick}) {
	switch (type) {
		case 'default':
			return (
				<div className={styles.default}>
					<div className={styles.default__wrapper}>
					</div>
				</div>
			  )
		case 'normis':
			return (
				<div 
					className={`${styles.normis} ${isFocused ? styles.normis__focused : ''}`} 
					onClick={() => onClick(item)}
					ref={outsideClick}
				>
					<p className={styles.normis__text}>{item.name}</p>
					<div style={{width: 'fit-content', display: 'flex', alignItems: 'center', gap: '1px'}}>
						<img src={isFocused ? star_white : star_black} alt="star" className={styles.normis__icon}/>
						<p className={styles.normis__rating}>{item.rating.toFixed(1)}</p>
					</div>
					<img src={isFocused ? bottom_center_black : bottom_center_white} alt="bottom_center" className={styles.normis__corner}/>
					{item.in_collection && <img src={isFocused ? bookmark_white : bookmark_black} alt="bookmark" className={`${styles.normis__bookmark} ${isFocused ? styles.normis__bookmark__focused : ''}`}/>}
				</div>
			)
		case 'sexy':
			return (
				<>
				</>
			)
		default:
			return (
				<div className={styles.wrapperr}>
				  <img src={icon} alt="icon" />
				</div>
			  )
	}
}

export default Pin
