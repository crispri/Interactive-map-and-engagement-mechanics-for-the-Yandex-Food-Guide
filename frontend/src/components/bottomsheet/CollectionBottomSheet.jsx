import styles from './CollectionBottomSheet.module.scss'
import { BottomSheet } from 'react-spring-bottom-sheet'
import 'react-spring-bottom-sheet/dist/style.css'
import CollectionBottomSheetHeader from './CollectionBottomSheetHeader'
import { useDispatch, useSelector } from "react-redux";
import { getCollections, putInCollection } from '../../lib/restaurantsSlice';
import { useEffect, useMemo, useState } from 'react';
import wantToGo from '../../assets/want_to_go_bottomsheet.svg'
import wantToOrder from '../../assets/want_to_order_bottomsheet.svg'

const CollectionBottomSheet = ({ currentRest, newCollectionSetOpen, newCollectionRef, collectionOpen, collectionSetOpen, collectionRef }) => {

    const dispatch = useDispatch();

    const restaurants = useSelector((state) => state.restaurantsSlice.restaurants)
    const collections = useSelector((state) => state.restaurantsSlice.collections)

    const [selectedCollection, setSelectedCollection] = useState(null);

    const handleSelection = (id) => {
        setSelectedCollection(id);
    };

    function handleClick() {
        console.log(currentRest.id)
        dispatch(putInCollection({ id: selectedCollection, restaurantId: currentRest.id }))
        onDismiss()
    }

    console.log(collections)
    // debugger;

    const onDismiss = () => {
        collectionSetOpen(false);
    }
    const contentNode = useMemo(() => collections?.map((collection, index) => (
        <div key={index} className={styles.collection_items}>
            <div className={styles.collection_info}>
                {collection?.pre_created_collection_name === "Хочу сходить"
                    ?
                    <div className={styles.collection_image_hard}>
                        <img src={wantToGo} alt={collection.name} />
                    </div>
                    :
                    collection?.pre_created_collection_name === "Хочу заказать"
                        ?
                        <div className={styles.collection_image_hard}>
                            <img src={wantToOrder} alt={collection.name} />
                        </div>
                        :
                        collection?.picture
                        ?
                        <div className={styles.collection_image}>
                            <img src={collection.picture} alt={collection.name} />
                        </div>
                        :
                        <div className={styles.collection_image_hard}>
                        </div>
                }
                <div className={styles.collection_text}>
                    <span className={styles.collection_name}>{collection.name}</span>
                    {/* <span className={styles.collection_subtext}>{`Сохранено ${restaurants.length} мест`}</span> */}
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
                header={<CollectionBottomSheetHeader newCollectionSetOpen={newCollectionSetOpen} newCollectionRef={newCollectionRef} />}
                open={collectionOpen}
                snapPoints={({ maxHeight }) => [maxHeight * 0.8]}>
                <div className="collections-container">
                    {contentNode}
                </div>
                <button className={styles.save_button} style={
                   {
                    height: '56px',
                    display: 'block',
                    margin: 'auto',
                    backgroundColor: '#FCE000',
                    border: 'none',
                    padding: '10px 20px',
                    borderRadius: '12px',
                    cursor: 'pointer',
                    width: '90%',
                } 
                }
                onClick={handleClick}>Сохранить</button>
            </BottomSheet >
        </>
    )
}

export default CollectionBottomSheet;