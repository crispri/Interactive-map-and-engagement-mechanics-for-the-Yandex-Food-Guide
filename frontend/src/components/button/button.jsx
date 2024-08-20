import styles from './button.module.scss'

function Button({
	className,
	icon,
	onClick,
}) {
  return (
	<div className={className} onClick={onClick}>
	  <img src={icon} alt="icon" />
	</div>
  )
}

export default Button