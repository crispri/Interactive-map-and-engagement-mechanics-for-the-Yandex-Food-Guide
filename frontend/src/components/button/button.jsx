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
// curl 'http://51.250.39.97:8080/guide/v1/restaurants' \
//   -H 'Referer: http://localhost:5173/' \
//   -H 'Authorization: token' \
//   -H 'User-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1' \
//   -H 'Content-Type: application/json;charset=utf-8' \
//   --data-raw '{"lower_left_corner":{"lat":56.562308221456746,"lon":36.80247982617612},"top_right_corner":{"lat":54.744847233097076,"lon":38.28586537185843},"max_count":0}'