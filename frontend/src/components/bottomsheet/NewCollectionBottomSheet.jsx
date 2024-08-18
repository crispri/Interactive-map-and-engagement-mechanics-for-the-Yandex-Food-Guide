import './NewCollectionBottomSheet.module.scss'
import { BottomSheet } from 'react-spring-bottom-sheet'
import 'react-spring-bottom-sheet/dist/style.css'
import { useDispatch } from 'react-redux'
import { useState } from 'react'
import { createCollection, getCollections } from '../../lib/restaurantsSlice'


const NewCollectionBottomSheet = ({ newCollectionSetOpen, newCollectionOpen, newCollectionRef }) => {

    const dispatch = useDispatch();
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');

    const onDismiss = () => {
        newCollectionSetOpen(false);
    }
    const handleCreateCollection = () => {
        dispatch(createCollection({name, description}));
        dispatch(getCollections());
        onDismiss();
    };

    return (
        <>
            <BottomSheet
                ref={newCollectionRef}
                open={newCollectionOpen}
                onDismiss={onDismiss}
                header={<h1>Новая коллекция</h1>}
                defaultSnap={({ maxHeight }) => maxHeight * 0.05}
                snapPoints={({ maxHeight }) => [
                    maxHeight * 0.45,
                ]}>
                <div style={{ padding: '20px' }}>
                    <div style={{ marginBottom: '20px' }}>
                        <label>
                            <input
                                placeholder='Название коллекции'
                                type="text"
                                value={name}
                                onChange={(e) => setName(e.target.value)}
                                style={{
                                    width: '100%',
                                    padding: '10px',
                                    marginTop: '8px',
                                    borderRadius: '5px',
                                    border: '1px solid #ccc',
                                }}
                            />
                        </label>
                    </div>

                    <div style={{ marginBottom: '20px' }}>
                        <label>
                            <textarea
                                placeholder='Описание'
                                value={description}
                                onChange={(e) => {
                                    console.log(e.target.value)
                                    setDescription(e.target.value)
                                }
                                }
                                style={{
                                    width: '100%',
                                    padding: '10px',
                                    marginTop: '8px',
                                    borderRadius: '5px',
                                    border: '1px solid #ccc',
                                }}
                            />
                        </label>
                    </div>
                    <button
                        onClick={handleCreateCollection}
                        style={{
                            backgroundColor: '#ffc107',
                            border: 'none',
                            padding: '10px 20px',
                            borderRadius: '5px',
                            cursor: 'pointer',
                        }}
                    >
                        Создать
                    </button>
                </div>
            </BottomSheet>
        </>
    )


}

export default NewCollectionBottomSheet;