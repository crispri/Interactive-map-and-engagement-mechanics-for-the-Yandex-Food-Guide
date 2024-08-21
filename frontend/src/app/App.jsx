import {
  createBrowserRouter,
  Outlet,
  RouterProvider,
  useParams,
} from "react-router-dom";

import { lazy, useEffect } from "react";
import React from "react";
import { useDispatch } from "react-redux";
import useDebounce from '../lib/useDebounce'
import { getRestaurants } from '../lib/restaurantsSlice'

import MainPage from '../components/mainpage/MainPage.jsx';
import Navbar from '../components/navbar/Navbar.jsx'
import MyBottomSheet from '../components/bottomsheet/MyBottomSheet.jsx'
import CollectionBottomSheet from '../components/bottomsheet/CollectionBottomSheet.jsx'
import MapComponent from '../components/map/MapComponent.jsx';
import { useRef } from "react";
import RestaurantCard from "../components/card/RestaurantCard.jsx";
import { useSelector } from "react-redux";
// import SheetContent from "../components/sheetcontent/SheetContent.jsx";
import RestaurantFullView from "../components/fullview/RestaurantFullView.jsx";
import sample from '../assets/sample.jpeg'
import FiltersFullView from "../components/filter/FiltersFullView.jsx";
import NewCollectionBottomSheet from "../components/bottomsheet/NewCollectionBottomSheet.jsx";

const SheetContent = lazy(() => import('../components/sheetcontent/SheetContent.jsx'))
 
function App() {
  console.log(`23`);
  const sheetRef = useRef()
  const collectionRef = useRef()
  const newCollectionRef = useRef()

  const [collectionOpen, collectionSetOpen] = React.useState(false)
  const [newCollectionOpen, newCollectionSetOpen] = React.useState(false);

  const current_pin = useSelector((state) => state.restaurantsSlice.current_pin)
  const restaurants = useSelector((state) => state.restaurantsSlice.restaurants)
  const current_selection = useSelector((state) => state.restaurantsSlice.currentSelection)
  const isInCollection = useSelector((state) => state.restaurantsSlice.is_in_collection)
  // console.log(current_pin, restaurants.filter(el => el.name === current_pin?.name));
  const {restId} = useParams()

  const [currentRest, setCurrentRest] = React.useState('');

  const [location, setLocation] = React.useState({
    bounds: [
      [37.599736050115176, 55.787418790096474],
      [37.632437149726066, 55.747522844045875]
      // [36.80247982617612, 56.562308221456746],
      // [38.28586537185843, 54.744847233097076]
    ],
    center: [37.623082, 55.75254], // starting position [lng, lat]
    zoom: 10 // starting zoom
  });

  const [currentPolygon, setCurrentPolygon] = React.useState(location.bounds);
  const dispatch = useDispatch()
  const updateHandler = (obj) => {
    setCurrentPolygon(obj.location.bounds)
  }
  const debouncedValue = useDebounce(currentPolygon, 300);


  useEffect(() => {
    let body = {
      "lower_left_corner": {
        "lat": debouncedValue[1][1],
        "lon": debouncedValue[0][0]
      },
      "top_right_corner": {
        "lat": debouncedValue[0][1],
        "lon": debouncedValue[1][0]
      },
    }
    if (current_selection) {
      if (isInCollection) {
        ym(98116436,'reachGoal','collection_on_the_map_change_polygon');
      } else {
        ym(98116436,'reachGoal','selection_on_the_map_change_polygon');
      }
      body["filters"] = [{
        "property": "selection_id",
        "operator": "in",
        "value": [
          current_selection
        ]
      }]
    }
    if (isInCollection) {
      body["only_collections"] = true
    }
    dispatch(getRestaurants(body))
  }, [debouncedValue, current_selection, isInCollection])
  const router = createBrowserRouter([
    {
      path: "/",
      element: <MainPage/>
    },
    {
      path: "/restaurants",
      element: (
        <>
          <Navbar/>
          <MapComponent sheetRef={sheetRef} location={location} updateHandler={updateHandler} setLocation={setLocation}/>
          <MyBottomSheet sheetRef={sheetRef} content={<Outlet/>} debouncedValue={debouncedValue}/>
          <CollectionBottomSheet currentRest={currentRest} collectionSetOpen={collectionSetOpen} newCollectionSetOpen={newCollectionSetOpen} newCollectionRef={newCollectionRef} collectionOpen={collectionOpen} collectionRef={collectionRef}/>
          <NewCollectionBottomSheet newCollectionSetOpen={newCollectionSetOpen} newCollectionOpen={newCollectionOpen} newCollectionRef={newCollectionRef}/>
        </>
      ),
      children: [
        {
          index: true,
          element: <SheetContent currentRest={currentRest} setCurrentRest={setCurrentRest} collectionSetOpen={collectionSetOpen} collectionRef={collectionRef} restaurants={restaurants}/>
        },
        {
          path: 'map/:restId',
          element:  <RestaurantCard setCurrentRest={setCurrentRest} collectionSetOpen={collectionSetOpen} collectionRef={collectionRef} restaurantInfo={current_pin}/>
        },
        {
          path: ':restId',
          element: <RestaurantFullView sheetRef={sheetRef}/>
        },
        {
          path: 'filters',
          element: <FiltersFullView sheetRef={sheetRef}/>
        }
      ]
    },
  ]);

  return (
    <RouterProvider router={router} />
  )
}

export default App
