import React from 'react'
import Button from '../button/button'

import styles from './Navbar.module.scss'
import Glyph from '../../assets/Glyph.svg'
import { useDispatch, useSelector } from "react-redux";
import bookmark from '../../assets/bookmark.svg'
import bookmarkWhite from '../../assets/bookmark_white_edges.svg'
import { toggleIsInCollection } from '../../lib/restaurantsSlice.js'

function Navbar() {

  const isInCollection = useSelector((state) => state.restaurantsSlice.is_in_collection);

  const dispatch = useDispatch();

  function toggleButton() {
	dispatch(toggleIsInCollection());
  }

  return (
	<div className={styles.wrapper}>
	  <Button icon={Glyph}/>
	  <div className={styles.wrapper__text}>
		<p style={{fontWeight: "400", fontSize: "13px"}}>Ваше местоположение</p>
		<p style={{fontWeight: "500", fontSize: "16px"}}>Льва Толстого, 16</p>
	  </div>
	  {isInCollection ?
 		<Button className={styles.wrapper__in_collection} icon={bookmarkWhite} onClick={toggleButton}/>
	  :
	  	<Button className={styles.wrapper__not_in_collection} icon={bookmark} onClick={toggleButton}/>
	  }
	 
	</div>
  )
}

export default Navbar
