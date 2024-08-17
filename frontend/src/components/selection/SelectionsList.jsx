import styles from './SelectionsList.module.scss'
import { useDispatch, useSelector } from 'react-redux'
import { getSelection } from '../../lib/restaurantsSlice'

function SelectionsList() {
	const selections = useSelector((state) => state.restaurantsSlice.selections)
	const current_selection = useSelector((state) => state.restaurantsSlice.current_selection)
	const dispatch = useDispatch();

	function onSetSelection(id) {
		// dispatch(setSelection(id))
		console.log(id);
	}

	const cards = selections.map((el) => 
	(<div 
		key={el.id} 
		className={styles.card} 
		onClick={() => onSetSelection(el.id)}
		style={{width : current_selection === el.id ? '274px' : '146px'}}
		transition={{ duration: 0.3 }}
		>{el.name}
	</div>))
  return (
	<div className={styles.wrapper}>
		<div className={styles.categories}>
			{cards}
		</div>
	</div>
  )
}

export default SelectionsList
