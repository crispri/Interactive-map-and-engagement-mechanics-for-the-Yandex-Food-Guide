import React, { useEffect, useMemo } from 'react'
import ReactDOM from 'react-dom'
import App from './components/app/App.jsx'
import MyBottomSheet from './components/bottomsheet/MyBottomSheet.jsx'
import './index.css'
import { store } from './lib/store.js'
import { Provider, useDispatch } from 'react-redux'
import useDebounce from './lib/useDebounce.js'
import { getRestaurants } from './lib/restaurantsSlice.js'

import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";
import Pin from './components/pin/Pin.jsx'
import { _apiUrl, COORDINATES } from './assets/variables.js'

window.map = null;
main();
const LOCATION = {
  bounds: [
    [36.80247982617612, 56.562308221456746],
    [38.28586537185843, 54.744847233097076]
  ],
  // center: [37.623082, 55.75254], // starting position [lng, lat]
  zoom: 13 // starting zoom
};

async function main() {

const [ymaps3React] = await Promise.all([ymaps3.import('@yandex/ymaps3-reactify'), ymaps3.ready]);
const reactify = ymaps3React.reactify.bindTo(React, ReactDOM);
const {YMap, YMapDefaultSchemeLayer, YMapDefaultFeaturesLayer, YMapMarker, YMapControls, YMapListener} = reactify.module(ymaps3);
const {YMapGeolocationControl, YMapZoomControl} = reactify.module(await ymaps3.import('@yandex/ymaps3-controls@0.0.1'));

// кластеризация
const {YMapClusterer, clusterByGrid} = await ymaps3.import('@yandex/ymaps3-clusterer@0.0.1');

const TestComponent = () => {
  const [currentPolygon, setCurrentPolygon] = React.useState(LOCATION.bounds);
  const dispatch = useDispatch()
  const updateHandler = (obj) => {
    setCurrentPolygon(obj.location.bounds)
  }
  const debouncedValue = useDebounce(currentPolygon, 1000);

  useEffect(() => {
    console.log('debouncedValue', debouncedValue);
    dispatch(getRestaurants({
      "lower_left_corner": {
        "lat": debouncedValue[0][1],
        "lon": debouncedValue[0][0]
      },
      "top_right_corner": {
        "lat": debouncedValue[1][1],
        "lon": debouncedValue[1][0]
      },
      "max_count": 0
    }))
  }, [debouncedValue])

  const gridSizedMethod = useMemo(() => clusterByGrid({ gridSize: 64 }), []);
 return (
 <>
  <div style={{width: '100%', height: '100%'}}>
    <YMap location={LOCATION} showScaleInCopyrights={true} ref={(x) => (map = x)}>
      <YMapDefaultSchemeLayer />
      <YMapDefaultFeaturesLayer/>

      {/* <YMapClusterer 
        marker={marker} 
        cluster={cluster} 
        method={gridSizedMethod} 
        features={points} 
      /> */}

      {COORDINATES.map(el => (
        <YMapMarker coordinates={el} key={el}>
          <Pin/>
        </YMapMarker>
      ))}

      <YMapListener 
        onUpdate={updateHandler}
      />
      <YMapControls position="left">
          {/* Add the geolocation control to the map */}
          <YMapGeolocationControl/>
          <YMapZoomControl/>
      </YMapControls>
    </YMap>    
  </div>
  <MyBottomSheet />
 </>
 )
}
  const router = createBrowserRouter([
    {
      path: "/",
      element: <App/>
    },
    {
      path: "/map",
      element: 
      <>
        <TestComponent/>
        {/* <MyBottomSheet/> */}
      </>
    },
    {
      path: "/bottomsheet",
      element: <MyBottomSheet />
    }
  ]);


  ReactDOM.render( 
    <Provider store={store}>
      <RouterProvider router={router} />
    </Provider>
  , 
    document.getElementById('root')
  );
}