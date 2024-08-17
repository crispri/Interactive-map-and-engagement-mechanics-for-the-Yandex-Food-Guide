import './CollectionBottomsheet.module.scss'
import { BottomSheet } from 'react-spring-bottom-sheet'
import 'react-spring-bottom-sheet/dist/style.css'

const CollectionBottomsheet = () => {
    return (
        <>
            <BottomSheet
                ref={sheetRef}
                open={true}
                blocking={false}
                header={
                    <>
                        {shouldShowHeader ? <HeaderFilters debouncedValue={debouncedValue}></HeaderFilters> : null}
                    </>
                }
                defaultSnap={({ maxHeight }) => maxHeight * 0.05}
                snapPoints={({ maxHeight }) => [
                    maxHeight * 0.45,
                    maxHeight * 0.05,
                    maxHeight
                ]}
            // sibling={<SelectionsList/>}
            // header={<SelectionsList/>}
            >
                <div className="bottomsheet">
                    {content}
                </div>
            </BottomSheet >
        </>
    )
}

export default CollectionBottomsheet;