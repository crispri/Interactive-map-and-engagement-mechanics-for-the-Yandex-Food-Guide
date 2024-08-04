import React from 'react'
import ReactDOM from 'react-dom'
import App from './components/app/App.jsx'
import MyBottomSheet from './components/bottomsheet/MyBottomSheet.jsx'
import './index.css'
import { useState } from 'react'
import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";
import Pin from './components/pin/Pin.jsx'
import { COORDINATES } from './assets/variables.js'

window.map = null;
main();
const LOCATION = {
  center: [37.623082, 55.75254], // starting position [lng, lat]
  zoom: 12 // starting zoom
};

async function main() {

  const [ymaps3React] = await Promise.all([ymaps3.import('@yandex/ymaps3-reactify'), ymaps3.ready]);
  const reactify = ymaps3React.reactify.bindTo(React, ReactDOM);
  const {YMap, YMapDefaultSchemeLayer, YMapDefaultFeaturesLayer, YMapMarker, YMapControls} = reactify.module(ymaps3);
  const {YMapGeolocationControl, YMapZoomControl} = reactify.module(await ymaps3.import('@yandex/ymaps3-controls@0.0.1'));

  console.dir(ymaps3.YMap);

const TestComponent = () => {

 return (
  <div style={{width: '100%', height: '100%'}}>
    <YMap location={LOCATION} showScaleInCopyrights={true} ref={(x) => (map = x)}>
      <YMapDefaultSchemeLayer />
      <YMapDefaultFeaturesLayer/>
      {COORDINATES.map(el => (
        <YMapMarker coordinates={el} key={el}>
          <Pin/>
        </YMapMarker>
      ))}
      <YMapControls position="left">
          {/* Add the geolocation control to the map */}
          <YMapGeolocationControl />
          <YMapZoomControl />
      </YMapControls>
    </YMap>    
  </div>
 )
}
  const router = createBrowserRouter([
    {
      path: "/",
      element: <App/>
    },
    {
      path: "/map",
      element: <TestComponent/>
    },
    {
      path: "/combine",
      element: (
        <>
        <div style={{width: '100%', height: '100%'}}>
          <YMap location={LOCATION} showScaleInCopyrights={true} ref={(x) => (map = x)}>
            <YMapDefaultSchemeLayer />
          </YMap>
        </div>
        <MyBottomSheet />
        </>
      )
    },
    {
      path: "/bottomsheet",
      element: <MyBottomSheet />
    }
  ]);


ReactDOM.render( <RouterProvider router={router} />, 

document.getElementById('root')
);
}