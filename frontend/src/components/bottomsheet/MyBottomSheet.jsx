import './MyBottomSheet.css'
import { BottomSheet } from 'react-spring-bottom-sheet'
import 'react-spring-bottom-sheet/dist/style.css'
import sample from '../../assets/sample.jpeg'
import SheetContent from '../sheetcontent/SheetContent';
import RestaurantFullView from '../fullview/RestaurantFullView';
import { useRef, useState } from 'react';

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

  const sheetRef = useRef()
  const [id, setId] = useState(-1);

  let options = {
    root: document.querySelector("#key"),
    rootMargin: "0px",
    threshold: 0.8,
  };

  console.log(sheetRef.current);
  // let observer = new IntersectionObserver(callback, options);


  return (
    <>
      <BottomSheet 
        ref={sheetRef}
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
        {id !== -1 ? (
          <RestaurantFullView id={id} setId={setId} sheetRef={sheetRef}> </RestaurantFullView>
        ) : 
          <SheetContent cardInfos={cardInfos} setId={setId} sheetRef={sheetRef}></SheetContent>
        };
      </BottomSheet >
    </>
  )
}

export default MyBottomSheet