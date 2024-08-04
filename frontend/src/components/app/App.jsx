import mainScreen from '../../assets/main_screen.jpg'
import mapButton from '../../assets/map_button.svg'
import './App.css'
import { useNavigate } from 'react-router-dom';

function App() {
  const navigateTo = useNavigate();
  return (
    <>
      <div className='App'>
        <div className='screen'>
          <div className='map'>
            <img className="map_button" src={mapButton} onClick={() => navigateTo('/map')}/>
          </div>
          <img className="main_screen" src={mainScreen} alt="Main Screen" />
        </div> 
      </div>
    </>
  )
}

export default App
