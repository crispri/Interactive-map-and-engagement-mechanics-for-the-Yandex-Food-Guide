import App from './app/App.jsx'
import React, { Suspense, createContext } from 'react'
import ReactDOM from 'react-dom'

import { Provider } from 'react-redux';
import { store } from './lib/store.js';

import './index.css'
import useDebounce from './lib/useDebounce.js'
import { getRestaurants, setCurrentPin } from './lib/restaurantsSlice.js'


import Pin from './components/pin/Pin.jsx'
import { _apiUrl } from './assets/variables.js'
import useOutsideClick from './lib/useOutsideClick.js'
import {YMapsContext} from './contexts/YMapsContext.js'


const [ymaps3React] = await Promise.all([ymaps3.import('@yandex/ymaps3-reactify'), ymaps3.ready]);
const reactify = ymaps3React.reactify.bindTo(React, ReactDOM);


 
const maps = reactify.module(ymaps3);
const mapsControls = reactify.module(await ymaps3.import('@yandex/ymaps3-controls@0.0.1'));
window.map = null;

ReactDOM.render(
	<Suspense fallback={null}>
		<Provider store={store}>
			<YMapsContext.Provider value={{...maps, ...mapsControls}}>
				<App/>
			</YMapsContext.Provider>
		</Provider>
	</Suspense>
, document.getElementById('root'));