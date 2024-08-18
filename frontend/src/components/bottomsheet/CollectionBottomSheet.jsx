import styles from './CollectionBottomSheet.module.scss'
import { BottomSheet } from 'react-spring-bottom-sheet'
import 'react-spring-bottom-sheet/dist/style.css'
import CollectionBottomSheetHeader from './CollectionBottomSheetHeader'
import { useDispatch, useSelector } from "react-redux";
import { getCollections, putInCollection } from '../../lib/restaurantsSlice';
import { useEffect, useMemo, useState } from 'react';

const CollectionBottomSheet = ({ currentRest, newCollectionSetOpen, newCollectionRef, collectionOpen, collectionSetOpen, collectionRef}) => {

    const dispatch = useDispatch();

    const restaurants = useSelector((state) => state.restaurantsSlice.restaurants)
    const collections = useSelector((state) => state.restaurantsSlice.collections)

    const [selectedCollection, setSelectedCollection] = useState(null);

    const handleSelection = (id) => {
        setSelectedCollection(id);
    };

    function handleClick() {
        dispatch(putInCollection({id: selectedCollection, restaurantId: currentRest.id}))
    }

    console.log(collections)
    // debugger;

    const onDismiss = () => {
        collectionSetOpen(false);
    }

    const contentNode = useMemo(() => collections?.map((collection, index) => (
        <div key={index} className={styles.collection_items}>
            <div className={styles.collection_info}>
                <div className={styles.collection_image}>
                    <img src={collection?.picture} alt={collection.name} />
                </div>
                <div className={styles.collection_text}>
                    <span className={styles.collection_name}>{collection.name}</span>
                    <span className={styles.collection_subtext}>{`Сохранено ${restaurants.length} мест`}</span>
                </div>
            </div>
            <input
                type="radio"
                name="collection"
                checked={selectedCollection === collection.id}
                onChange={() => handleSelection(collection.id)}
                className={styles.collection_radio}
            />
        </div>
    )), [collections, selectedCollection]);

    // debugger;

    return (
        <>
            <BottomSheet
                ref={collectionRef}
                onDismiss={onDismiss}
                header={<CollectionBottomSheetHeader newCollectionSetOpen={newCollectionSetOpen} newCollectionRef={newCollectionRef}/>}
                open={collectionOpen}
                snapPoints={({ maxHeight }) => [maxHeight * 0.6]}>
                <div className="collections-container">
                    {contentNode}
                </div>
                <button className={styles.save_button} onClick={handleClick}>Сохранить</button>
            </BottomSheet >
        </>
    )
}

export default CollectionBottomSheet;