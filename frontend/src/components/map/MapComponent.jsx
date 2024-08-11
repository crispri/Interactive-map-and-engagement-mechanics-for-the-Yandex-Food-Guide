import React, { useContext, useEffect } from 'react'
import './Map.css'
import useDebounce from '../../lib/useDebounce'
import { getRestaurants } from '../../lib/restaurantsSlice'
import MyBottomSheet from '../bottomsheet/MyBottomSheet'

import Pin from '../pin/Pin'
import { useDispatch, useSelector } from 'react-redux'
import { setCurrentPin } from '../../lib/restaurantsSlice'


import useOutsideClick from '../../lib/useOutsideClick'
import {YMapsContext} from '../../contexts/YMapsContext'

const MapComponent = ({sheetRef}) => {
  const { 
    YMap, 
    YMapDefaultSchemeLayer, 
    YMapDefaultFeaturesLayer, 
    YMapControls, 
    YMapZoomControl, 
    YMapListener, 
    YMapMarker, 
    YMapGeolocationControl 
  }  = useContext(YMapsContext);

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
  const restaurants = useSelector((state) => state.restaurantsSlice.restaurants).map(el => {
    return ({
      ...el,
      coordinates: [el.coordinates.lon, el.coordinates.lat],
    })
  })

  const current_pin = useSelector((state) => state.restaurantsSlice.current_pin)

  const setFocus = (pin) => {
    // console.log(pin);
    dispatch(setCurrentPin(pin))
    setLocation(loco => ({
      center: [pin.coordinates[0], pin.coordinates[1]],
      duration: 300,
    }))
    sheetRef.current.snapTo(({ maxHeight }) => maxHeight * 0.4);
  }

  const onOutsideClick = useOutsideClick(() => {
    dispatch(setCurrentPin(null))
    sheetRef.current.snapTo(({ maxHeight }) =>  maxHeight * 0.05);
  })

  useEffect(() => {
    dispatch(getRestaurants({
      "lower_left_corner": {
        "lat": debouncedValue[1][1],
        "lon": debouncedValue[0][0]
      },
      "top_right_corner": {
        "lat": debouncedValue[0][1],
        "lon": debouncedValue[1][0]
      },
      "max_count": 0
    }))
  }, [debouncedValue])

 return (
 <>
  <div style={{width: '100%', height: '100%'}}>
    <YMap location={location} showScaleInCopyrights={true} ref={(x) => (map = x)}>
      <YMapDefaultSchemeLayer />
      <YMapDefaultFeaturesLayer/>

      {restaurants.map(el => (
        <YMapMarker coordinates={el.coordinates} key={el.id}>
          <Pin 
            type={'normis'} 
            item={el} 
            isFocused={current_pin?.id === el.id} 
            onClick={setFocus}
            outsideClick={onOutsideClick}
          />
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

export default MapComponent