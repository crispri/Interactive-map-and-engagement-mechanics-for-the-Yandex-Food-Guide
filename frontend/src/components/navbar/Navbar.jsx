import React from 'react'
import Button from '../button/button'

import styles from './Navbar.module.scss'
import Glyph from '../../assets/Glyph.svg'
import bookmark from '../../assets/bookmark.svg'

function Navbar() {
  return (
	<div className={styles.wrapper}>
	  <Button icon={Glyph}/>
	  <div className={styles.wrapper__text}>
		<p style={{fontWeight: "400", fontSize: "13px"}}>Ваше местоположение</p>
		<p style={{fontWeight: "500", fontSize: "16px"}}>Льва Толстого, 16</p>
	  </div>
	  <Button icon={bookmark}/>
	</div>
  )
}

export default Navbar
