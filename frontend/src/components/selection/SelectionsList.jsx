import styles from './SelectionsList.module.scss'
import { useSelector } from 'react-redux'

function SelectionsList() {
	const selections = useSelector((state) => state.restaurantsSlice.selections)

	const cards = selections.map((el) => 
	(<div 
		key={el.id} 
		className={styles.card} 
		onClick={() => {console.log('clicked');}}
		>{el.name}</div>))
  return (
	<div className={styles.wrapper}>
		<div className={styles.categories}>
			{cards}
		</div>
	</div>
  )
}

export default SelectionsList
