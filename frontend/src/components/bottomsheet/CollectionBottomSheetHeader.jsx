import './CollectionBottomSheet.module.scss'
import 'react-spring-bottom-sheet/dist/style.css'
import styles from './CollectionBottomSheet.module.scss';

const CollectionBottomSheetHeader = ({ newCollectionSetOpen, newCollectionRef }) => {

    function handleClick() {
        console.log('add new collection');
        newCollectionSetOpen(true);
        // newCollectionRef.current.snapTo();
    }

    return (
        <>
            <div className={styles.inline}>
                <h1>
                    Коллекции
                </h1>
                <h1 onClick={handleClick}>
                    +
                </h1>
            </div>
        </>
    )
}

export default CollectionBottomSheetHeader;