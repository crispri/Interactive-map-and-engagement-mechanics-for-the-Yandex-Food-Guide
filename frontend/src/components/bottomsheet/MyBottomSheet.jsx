import './MyBottomSheet.css'
import { BottomSheet } from 'react-spring-bottom-sheet'
import 'react-spring-bottom-sheet/dist/style.css'
import sample from '../../assets/sample.jpeg'
import SheetContent from '../sheetcontent/SheetContent';

const cardInfos = [
{
  title: "Raw To Go Kitchen",
  description: "«Raw to Go» — это кафе, которое специализируется на веганской и сырой кухне",
  openUntil: "До 22:00",
  tags: ["1000-2500 ₽", "Японская", "Веганская", "Детское меню", "Кафе"]
},
{
  title: "Raw To Go Kitchen2",
  description: "«Raw to Go» — это кафе, которое специализируется на веганской и сырой кухне",
  openUntil: "До 22:00",
  tags: ["1000-2500 ₽", "Японская", "Веганская", "Детское меню", "Кафе"]
},
{
  title: "Raw To Go Kitchen3",
  description: "«Raw to Go» — это кафе, которое специализируется на веганской и сырой кухне",
  openUntil: "До 22:00",
  tags: ["1000-2500 ₽", "Японская", "Веганская", "Детское меню", "Кафе"]
}
];

const MyBottomSheet = () => {
  return (
    <>
      <BottomSheet 
        open={true}
        blocking={false}
        defaultSnap={({ maxHeight }) => maxHeight * 0.05}
        snapPoints={({ maxHeight }) => [
          maxHeight / 3 * 2,
          maxHeight * 0.05,
          maxHeight
        ]}
      >
        <img src={sample} alt="sample" style={{width: '100%', height: '100%'}}/>
        <SheetContent cardInfos={cardInfos}></SheetContent>
      </BottomSheet >
    </>
  )
}

export default MyBottomSheet