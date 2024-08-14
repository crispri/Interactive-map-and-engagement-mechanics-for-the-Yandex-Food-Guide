import styles from './button.module.scss'

function Button({
	icon,
	onClick,
}) {
  return (
	<div className={styles.wrapper}>
	  <img src={icon} alt="icon" />
	</div>
  )
}

export default Button