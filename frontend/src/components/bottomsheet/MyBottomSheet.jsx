import './MyBottomSheet.css'
import { BottomSheet } from 'react-spring-bottom-sheet'
import 'react-spring-bottom-sheet/dist/style.css'

const MyBottomSheet = () => {
  return (
    <>
      <BottomSheet 
        open={true}
        snapPoints={({ minHeight }) => minHeight}
      >
        My bottomsheet
      </BottomSheet>
    </>
  )
}

export default MyBottomSheet