import './MyBottomSheet.css'
import { BottomSheet } from 'react-spring-bottom-sheet'
import 'react-spring-bottom-sheet/dist/style.css'
import sample from '../../assets/sample.jpeg'

const MyBottomSheet = () => {
  return (
    <>
      <BottomSheet 
        open={true}
        snapPoints={({ minHeight }) => minHeight}
      >
        <img src={sample} alt="sample" style={{width: '100%', height: '100%'}}/>
      </BottomSheet>
    </>
  )
}

export default MyBottomSheet