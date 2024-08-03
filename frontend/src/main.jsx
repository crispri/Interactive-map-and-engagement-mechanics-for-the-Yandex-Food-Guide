import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './components/app/App.jsx'
import Map from './components/map/Map.jsx'
import MyBottomSheet from './components/bottomsheet/MyBottomSheet.jsx'
import './index.css'
import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";

const router = createBrowserRouter([
  {
    path: "/",
    element: <App />
  },
  {
    path: "/map",
    element: <Map />
  },
  {
    path: "/bottomsheet",
    element: <MyBottomSheet />
  }
]);

ReactDOM.createRoot(document.getElementById('root')).render(
    <RouterProvider router={router} />,
)
