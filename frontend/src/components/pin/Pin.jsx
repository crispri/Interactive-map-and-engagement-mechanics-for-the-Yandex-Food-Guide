import React from 'react'
import icon from '../../assets/fork_knife.svg'
import styles from './Pin.module.scss'
import star_black from '../../assets/star_black.svg'
import bottom_center from '../../assets/BottomCenter.svg'
import bookmark_black from '../../assets/bookmark_black.svg'

function Pin({type, item}) {
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
				<div className={styles.normis}>
					<p className={styles.normis__text}>{item.name}</p>
					<div style={{width: 'fit-content', display: 'flex', alignItems: 'center', gap: '1px'}}>
						<img src={star_black} alt="star" className={styles.normis__icon}/>
						<p className={styles.normis__rating}>{item.rating.toFixed(1)}</p>
					</div>
					<img src={bottom_center} alt="bottom_center" className={styles.normis__corner}/>
					{item.is_favorite && <img src={bookmark_black} alt="bookmark" className={styles.normis__bookmark}/>}
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
