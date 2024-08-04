//
//  MapManager.swift
//  iosApp
//
//  Created by Максим Кузнецов on 03.08.2024.
//

import Foundation
import CoreLocation
import YandexMapsMobile

class MapManager: NSObject, CLLocationManagerDelegate, ObservableObject {
    private let manager = CLLocationManager()
    let mapView = YMKMapView(frame: CGRect.zero)
    @Published var lastUserLocation: CLLocation? = nil
    lazy var map : YMKMap = {
        return mapView?.mapWindow.map ?? .init()
    }()
    
    override init() {
        super.init()
        self.manager.delegate = self
        manager.requestAlwaysAuthorization()
        manager.startUpdatingLocation()
    }
    
    func currentUserLocation() {
        if let myLocation = lastUserLocation {
            centerMapLocation(
                target: YMKPoint(
                    latitude: myLocation.coordinate.latitude,
                    longitude: myLocation.coordinate.longitude
                ),
                map: mapView ?? .init()
            )
        } else if let myLocation = manager.location {
            centerMapLocation(
                target: YMKPoint(
                    latitude: myLocation.coordinate.latitude,
                    longitude: myLocation.coordinate.longitude
                ),
                map: mapView ?? .init()
            )
        } else {
            print("Failed to get user location")
        }
    }
    
    func centerMapLocation(target location: YMKPoint?, map: YMKMapView) {
        guard let location = location else {
            print("Failed to get user location")
            return
        }
        
        map.mapWindow.map.move(
            with: YMKCameraPosition(target: location, zoom: 18, azimuth: 0, tilt: 0),
            animation: YMKAnimation(type: YMKAnimationType.smooth, duration: 0.5)
        )
    }
    
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        if status == .authorizedWhenInUse {
            self.manager.startUpdatingLocation()
        }
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        self.lastUserLocation = locations.last
    }
    
    func addPoints() {
        let iconStyle = YMKIconStyle()
        let image = UIImage(named: "pin") ?? UIImage()
        
        let placemark1 = map.mapObjects.addPlacemark()
        placemark1.geometry = .init(latitude: 55.733766, longitude: 37.589872)
        placemark1.setIconWith(image, style: iconStyle)
        
        let placemark2 = map.mapObjects.addPlacemark()
        placemark2.geometry = .init(latitude: 55.732964, longitude: 37.589473)
        placemark2.setIconWith(image, style: iconStyle)
        
        let placemark3 = map.mapObjects.addPlacemark()
        placemark3.geometry = .init(latitude: 55.733240, longitude: 37.590344)
        placemark3.setIconWith(image, style: iconStyle)
    }
}
