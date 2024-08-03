import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './components/app/App.jsx'
import Map from './components/map/Map.jsx'
import BottomSheet from './components/bottomsheet/BottomSheet.jsx'
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
    element: <BottomSheet />
  }
]);

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>,
)
