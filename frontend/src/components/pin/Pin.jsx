import React from 'react'
import icon from '../../assets/fork_knife.svg'
import styles from './Pin.module.scss'
import star_black from '../../assets/star_black.svg'
import bottom_center_white from '../../assets/BottomCenter_white.svg'
import bottom_center_black from '../../assets/BottomCenter_black.svg'
import bookmark_black from '../../assets/bookmark_black.svg'
import star_white from '../../assets/star_white.svg'
import bookmark_white from '../../assets/bookmark_white.svg'
import sample_sexy from '../../assets/sample_sexy_image.png'

import ultima_svg from '../../assets/ultima_svg.svg'
import open_kitchen_svg from '../../assets/kitchen_svg.svg'

function Pin({type, item, isFocused, onClick, outsideClick, refProp, id}) {
	let tag 
	if (item.tags.includes('ULTIMA GUIDE')){
		tag = 'ultima'
	} else if (item.tags.includes('Открытая кухня')) {
		tag = 'open_kitchen'
	} 
	switch (type) {
		case 'default': 
		if (tag) {
			if (tag === 'ultima') {
				return (
					<div className={styles.default} ref={refProp} id={id}>
						<div style={{padding: '1px', backgroundColor: "#21201F", borderRadius: '6px', width: '16px', height: '16px'}}>
							<img src={ultima_svg} alt="ultima" style={{width: '100%', height: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center'}}/>
						</div>
					</div>
				)
			}
			return (
				<div className={styles.default} ref={refProp} id={id}>
					<div style={{padding: '1px', backgroundColor: "#FFE033", borderRadius: '6px', width: '16px', height: '16px'}}>
						<img src={open_kitchen_svg} alt="kitchen" style={{width: '100%', height: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center'}}/>
					</div>
				</div>
			)
		}
			return (
				<div className={styles.default} ref={refProp} id={id}>
					<div className={styles.default__wrapper}>
					</div>
				</div>
			  )
		case 'normis':
			return (
				<div ref={refProp}>
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
				</div>
			)
		case 'sexy':
			return (
				<div ref={refProp} id={id}>
					<div 
					className={`${styles.sexy__container}`} 
					onClick={() => onClick(item)}
					ref={outsideClick}
					>
						<div className={`${styles.sexy} ${isFocused ? styles.sexy__focused : ''}`}>
							<div className={styles.sexy__wrapper}>
								<p className={styles.normis__text}>{item.name}</p>
								<div style={{width: 'fit-content', display: 'flex', alignItems: 'center', gap: '1px'}}>
									<img src={isFocused ? star_white : star_black} alt="star" className={styles.normis__icon}/>
									<p className={styles.normis__rating}>{item.rating.toFixed(1)}</p>
								</div>
							</div>
							<p className={styles.sexy__descr}>{item.additional_info}</p>
							<img src={isFocused ? bottom_center_black : bottom_center_white} alt="bottom_center" className={styles.normis__corner}/>
							{item.in_collection && <img src={isFocused ? bookmark_white : bookmark_black} alt="bookmark" className={`${styles.normis__bookmark} ${isFocused ? styles.normis__bookmark__focused : ''}`}/>}
						</div>
						<img src={item.food} alt="sexy_photo" className={styles.sexy_photo}/>
						<div className={styles.sexy__tags}>
							<div className={styles.default} ref={refProp} id={id}>
								<div style={{padding: '1px', backgroundColor: "#21201F", borderRadius: '6px', width: '16px', height: '16px'}}>
									<img src={ultima_svg} alt="ultima" style={{width: '100%', height: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center'}}/>
								</div>
							</div>
							<div className={styles.default} ref={refProp} id={id}>
								<div style={{padding: '1px', backgroundColor: "#FFE033", borderRadius: '6px', width: '16px', height: '16px'}}>
									<img src={open_kitchen_svg} alt="kitchen" style={{width: '100%', height: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center'}}/>
								</div>
							</div>
						</div>
					</div>
				</div>
			)
		default:
			return (
				<div className={styles.wrapperr} id={id}>
				  <img src={icon} alt="icon" />
				</div>
			  )
	}
}

export default Pin
