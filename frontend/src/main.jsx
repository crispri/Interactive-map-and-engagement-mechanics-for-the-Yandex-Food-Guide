import React from 'react'
import ReactDOM from 'react-dom/client'
import Map from './components/map/Map.jsx'

import BottomSheet from './components/bottomsheet/BottomSheet.jsx'
import './index.css'
import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";

window.map = null;
main();
const LOCATION = {
  center: [37.623082, 55.75254], // starting position [lng, lat]
  zoom: 9 // starting zoom
};

async function main() {

  const [ymaps3React] = await Promise.all([ymaps3.import('@yandex/ymaps3-reactify'), ymaps3.ready]);
  const reactify = ymaps3React.reactify.bindTo(React, ReactDOM);
  const {YMap, YMapDefaultSchemeLayer} = reactify.module(ymaps3);

  const router = createBrowserRouter([
    {
      path: "/",
      element: (
        <div style={{width: '100%', height: '100%'}}>
          <YMap location={LOCATION} showScaleInCopyrights={true} ref={(x) => (map = x)}>
            <YMapDefaultSchemeLayer />
          </YMap>
        </div>
      )
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
}

