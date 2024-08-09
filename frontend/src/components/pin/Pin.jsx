import React from 'react'
import icon from '../../assets/fork_knife.svg'
import styles from './Pin.module.scss'

function Pin() {
  return (
	<div className={styles.wrapper}>
	  <img src={icon} alt="icon" />
	</div>
  )
}

export default Pin
