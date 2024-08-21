import './CollectionBottomSheet.module.scss'
import 'react-spring-bottom-sheet/dist/style.css'
import styles from './CollectionBottomSheet.module.scss';
import plus from '../../assets/plus_icon.svg'

const CollectionBottomSheetHeader = ({ newCollectionSetOpen, newCollectionRef }) => {

    function handleClick() {
        console.log('add new collection');
        newCollectionSetOpen(true);
        // newCollectionRef.current.snapTo();
    }

    return (
        <>
            <div className={styles.collection_items}>
                <h1>
                    Коллекции
                </h1>
                <h1 onClick={handleClick}>
                    <img src={plus} alt='add collection'/>
                </h1>
            </div>
        </>
    )
}

export default CollectionBottomSheetHeader;