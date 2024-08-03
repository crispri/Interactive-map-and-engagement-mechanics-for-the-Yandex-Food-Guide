import { useState } from 'react'
import reactLogo from '../../assets/react.svg'
import viteLogo from '../../../public/vite.svg'
import './App.css'

const LOCATION = {
  center: [37.623082, 55.75254], // starting position [lng, lat]
  zoom: 9 // starting zoom
};

function App() {
  
  return (
    <div style={{width: '100%', height: '100%'}}>
      <YMap location={LOCATION} showScaleInCopyrights={true} ref={(x) => (map = x)}>
        <YMapDefaultSchemeLayer />
      </YMap>
    </div>
  )

}

export default App
