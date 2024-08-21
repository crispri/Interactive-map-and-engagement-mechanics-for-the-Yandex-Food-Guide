import './MyBottomSheet.css'
import { BottomSheet } from 'react-spring-bottom-sheet'
import 'react-spring-bottom-sheet/dist/style.css'
import sample from '../../assets/sample.jpeg'
import SheetContent from '../sheetcontent/SheetContent';
import { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { getSelections } from '../../lib/restaurantsSlice';
import SelectionsList from '../selection/SelectionsList';
import CollectionsList from '../collection/CollectionsList';
import HeaderFilters from '../filter/HeaderFilters';
import { useLocation } from 'react-router-dom';

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

const MyBottomSheet = ({sheetRef, content, debouncedValue, onSpringEnd}) => {
  const location = useLocation(); 
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(getSelections())
  }, [])

  const isInCollection = useSelector((state) => state.restaurantsSlice.is_in_collection);
  const shouldShowHeader = location.pathname === '/restaurants';  

  return (
    <BottomSheet 
      ref={sheetRef}
      open={true}
      blocking={false}
      header={
        <>
        { isInCollection ?
          <CollectionsList/>
          :
          <SelectionsList/>
        }
        {/* {shouldShowHeader ? <HeaderFilters debouncedValue={debouncedValue}></HeaderFilters> : null} */}
        </>

      }
      defaultSnap={({ maxHeight }) => maxHeight * 0.05}
      snapPoints={({ maxHeight }) => {
        return [
          maxHeight * 0.45,
          maxHeight * 0.05,
          maxHeight
        ]
      }}
      onSpringEnd={onSpringEnd}
      // sibling={<SelectionsList/>}
      // header={<SelectionsList/>}
    >
      <div className="bottomsheet">
        {content}
      </div>
    </BottomSheet >
  )
}

export default MyBottomSheet