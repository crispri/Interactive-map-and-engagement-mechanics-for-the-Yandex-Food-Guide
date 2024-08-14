import {
  createBrowserRouter,
  Outlet,
  RouterProvider,
  useParams,
} from "react-router-dom";

import MainPage from '../components/mainpage/MainPage.jsx';
import Navbar from '../components/navbar/Navbar.jsx'
import MyBottomSheet from '../components/bottomsheet/MyBottomSheet.jsx'
import MapComponent from '../components/map/MapComponent.jsx';
import { useRef } from "react";
import RestaurantCard from "../components/card/RestaurantCard.jsx";
import { useSelector } from "react-redux";
import SheetContent from "../components/sheetcontent/SheetContent.jsx";
import RestaurantFullView from "../components/fullview/RestaurantFullView.jsx";
import sample from '../assets/sample.jpeg'

 
function App() {
  const sheetRef = useRef()
  const current_pin = useSelector((state) => state.restaurantsSlice.current_pin)
  const restaurants = useSelector((state) => state.restaurantsSlice.restaurants)
  // console.log(current_pin, restaurants.filter(el => el.name === current_pin?.name));
  const {restId} = useParams()

  const router = createBrowserRouter([
    {
      path: "/",
      element: <MainPage/>
    },
    {
      path: "/restaurants/",
      element: (
        <>
          <Navbar/>
          <MapComponent sheetRef={sheetRef}/>
          <MyBottomSheet sheetRef={sheetRef} content={<Outlet/>}/>
        </>
      ),
      children: [
        {
          index: true,
          element: <SheetContent></SheetContent>
        },
        {
          path: ':restId',
          element: <RestaurantFullView sheetRef={sheetRef}></RestaurantFullView>
        },
        {
          path: 'map/:restId',
          element:  <RestaurantCard restaurantInfo={current_pin}/>
        },
      ]
    },
  ]);

  return (
    <RouterProvider router={router} />
  )
}

export default App
