import './AddCollectionBottomsheet.module.scss'
import { BottomSheet } from 'react-spring-bottom-sheet'
import 'react-spring-bottom-sheet/dist/style.css'

const AddCollectionBottomSheet = () => {
    return (
        <>
            <BottomSheet
                ref={sheetRef}
                open={true}
                blocking={false}
                header={
                    <h1>Добавить коллецию
                    </h1>
                }
                defaultSnap={({ maxHeight }) => maxHeight * 0.05}
                snapPoints={({ maxHeight }) => [
                    maxHeight * 0.45,
                ]}
            // sibling={<SelectionsList/>}
            // header={<SelectionsList/>}
            >
                <div className="bottomsheet">
                    {content}
                </div>
                <div>
                    
                </div>
            </BottomSheet >
        </>
    )
}

export default AddCollectionBottomSheet;