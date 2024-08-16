import './MyBottomSheet.css'
import { BottomSheet } from 'react-spring-bottom-sheet'
import 'react-spring-bottom-sheet/dist/style.css'
import sample from '../../assets/sample.jpeg'
import SheetContent from '../sheetcontent/SheetContent';
import { useEffect, useState } from 'react';
import { useDispatch } from 'react-redux';
import { getSelections } from '../../lib/restaurantsSlice';
import SelectionsList from '../selection/SelectionsList';

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
  const dispatch = useDispatch()

  useEffect(() => {
    dispatch(getSelections())
  }, [])

  return (
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
      // sibling={<SelectionsList/>}
      header={<SelectionsList/>}
    >
      <div className="bottomsheet">
        {content}
      </div>
    </BottomSheet >
  )
}

export default MyBottomSheet