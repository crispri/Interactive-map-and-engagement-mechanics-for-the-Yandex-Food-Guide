import React from 'react'
import Button from '../button/button'

import styles from './Navbar.module.scss'
import Glyph from '../../assets/Glyph.svg'
import { useDispatch, useSelector } from "react-redux";
import bookmarkPressed from '../../assets/bookmark_pressed.svg'
import bookmarkUnpressed from '../../assets/bookmark_unpressed.svg'
import { getCollections, getSelections, toggleIsInCollection } from '../../lib/restaurantsSlice.js'

function Navbar() {

  const isInCollection = useSelector((state) => state.restaurantsSlice.is_in_collection);

  const dispatch = useDispatch();

  async function toggleButton() {
	await dispatch(toggleIsInCollection());
	if (isInCollection === true) {
		dispatch(getCollections());
	} else {
		dispatch(getSelections());
	}
  }

  return (
	<div className={styles.wrapper}>
	  <Button icon={Glyph}/>
	  <div className={styles.wrapper__text}>
		<p style={{fontWeight: "400", fontSize: "13px"}}>Ваше местоположение</p>
		<p style={{fontWeight: "500", fontSize: "16px"}}>Льва Толстого, 16</p>
	  </div>
	  {isInCollection ?
 		<Button className={styles.wrapper__in_collection} icon={bookmarkPressed} onClick={toggleButton}/>
	  :
	  	<Button className={styles.wrapper__not_in_collection} icon={bookmarkUnpressed} onClick={toggleButton}/>
	  }
	 
	</div>
  )
}

export default Navbar
