import { useNavigate } from 'react-router-dom';
import mainScreen from '../../assets/main_screen.jpg'
import mapButton from '../../assets/map_button.svg'
import './App.css'
import React from 'react'

function MainPage() {
	const navigateTo = useNavigate();
  return (
	<div className='App'>
        <div className='screen'>
          <div className='map'>
            <img className="map_button" src={mapButton} onClick={() => navigateTo('/restaurants')}/>
          </div>
          <img className="main_screen" src={mainScreen} alt="Main Screen" />
        </div> 
      </div>
  )
}

export default MainPage
