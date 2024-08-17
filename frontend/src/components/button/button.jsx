import styles from './button.module.scss'

function Button({
	icon,
	onClick,
}) {
  return (
	<div className={styles.wrapper} onClick={onClick}>
	  <img src={icon} alt="icon" />
	</div>
  )
}

export default Button