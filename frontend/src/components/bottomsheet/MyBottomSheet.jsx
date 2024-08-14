import './MyBottomSheet.css'
import { BottomSheet } from 'react-spring-bottom-sheet'
import 'react-spring-bottom-sheet/dist/style.css'
import sample from '../../assets/sample.jpeg'
import SheetContent from '../sheetcontent/SheetContent';
import { useState } from 'react';

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

const MyBottomSheet = ({sheetRef, content}) => {

  const [id, setId] = useState(-1);

  let options = {
    root: document.querySelector("#key"),
    rootMargin: "0px",
    threshold: 0.8,
  };
  // let observer = new IntersectionObserver(callback, options);

  return (
    <>
      <BottomSheet 
        ref={sheetRef}
        open={true}
        blocking={false}
        defaultSnap={({ maxHeight }) => maxHeight * 0.05}
        snapPoints={({ maxHeight }) => [
          maxHeight * 0.45,
          maxHeight * 0.05,
          maxHeight
        ]}
      >
        <div className="bottomsheet">
        {content}
        </div>
      </BottomSheet >
    </>
  )
}

export default MyBottomSheet