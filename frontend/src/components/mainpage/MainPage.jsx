import { useNavigate } from 'react-router-dom';
import mainScreen from '../../assets/main_screen.jpg'
import mapButton from '../../assets/map_button.svg'
import './App.css'
import React from 'react'
import { useDispatch } from 'react-redux';
import { login } from '../../lib/restaurantsSlice';

function MainPage() {
  const dispatсh = useDispatch();
	const navigateTo = useNavigate();
  return (
	<div className='App'>
        <div className='screen'>
          <div className='map'>
            <img className="map_button" src={mapButton} onClick={() => {
              console.log('test1')
              ym(98116436,'reachGoal','main_screen_button_open_map');
              console.log('test2')
              navigateTo('/restaurants');
            }}/>
          </div>
          <img className="main_screen" src={mainScreen} alt="Main Screen" />
        </div> 
      </div>
  )
}

export default MainPage
