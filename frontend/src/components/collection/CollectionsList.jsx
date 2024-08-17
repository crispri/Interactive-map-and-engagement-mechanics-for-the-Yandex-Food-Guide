import styles from './CollectionsList.module.scss'
import { useDispatch, useSelector } from 'react-redux'
import { getCollection } from '../../lib/restaurantsSlice'

function CollectionsList() {
	const collections = useSelector((state) => state.restaurantsSlice.collections)
	const current_collection = useSelector((state) => state.restaurantsSlice.current_collection)
	const dispatch = useDispatch();

	function onSetCollection(id) {
		dispatch(getCollection(id))
		// console.log(id);
	}

	const cards = collections.map((el) => 
	(<div 
		key={el.id} 
		className={styles.card} 
		onClick={() => onSetCollection(el.id)}
		style={{width : current_collection === el.id ? '274px' : '146px'}}
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

export default CollectionsList;
