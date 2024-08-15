import React, { useContext, useEffect } from 'react'
import './Map.css'
import { useDispatch } from 'react-redux'

import Pin from '../pin/Pin'
import {useSelector } from 'react-redux'
import { setCurrentPin } from '../../lib/restaurantsSlice'


import useOutsideClick from '../../lib/useOutsideClick'
import {YMapsContext} from '../../contexts/YMapsContext'
import { useNavigate } from 'react-router-dom'

const MapComponent = ({sheetRef, location, updateHandler, setLocation}) => {
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

  const current_pin = useSelector((state) => state.restaurantsSlice.current_pin)
  const navigate = useNavigate()
  const restaurants = useSelector((state) => state.restaurantsSlice.restaurants).map(el => {
    return ({
      ...el,
      coordinates: [el.coordinates.lon, el.coordinates.lat],
    })
  })
  const dispatch = useDispatch()

  const setFocus = (pin) => {
    dispatch(setCurrentPin(pin))
    setLocation(loco => ({
      center: [pin.coordinates[0], pin.coordinates[1]],
      duration: 300,
    }))
    navigate(`map/${pin.id}`)
    sheetRef.current.snapTo(({ maxHeight }) => maxHeight * 0.45);
  }

  const onOutsideClick = useOutsideClick(() => {
    navigate(`/restaurants`)
    sheetRef.current.snapTo(({ maxHeight }) =>  maxHeight * 0.05);
    dispatch(setCurrentPin(null))
  })

 return (
 <>
  <div style={{width: '100%', height: '100%', paddingBottom: '40px'}}>
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
 </>
 )
}

export default MapComponent