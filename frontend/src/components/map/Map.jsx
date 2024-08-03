import './Map.css'

const LOCATION = {
  center: [37.623082, 55.75254], // starting position [lng, lat]
  zoom: 9 // starting zoom
};

async function Map() {

  const [ymaps3React] = await Promise.all([ymaps3.import('@yandex/ymaps3-reactify'), ymaps3.ready]);
  const reactify = ymaps3React.reactify.bindTo(React, ReactDOM);
  const {YMap, YMapDefaultSchemeLayer} = reactify.module(ymaps3);

  return (
    <div style={{width: '100%', height: '100%'}}>
      <YMap location={LOCATION} showScaleInCopyrights={true} ref={(x) => (map = x)}>
        {/* Add a map scheme layer */}
        <YMapDefaultSchemeLayer />
      </YMap>
    </div>
  )
}

export default Map