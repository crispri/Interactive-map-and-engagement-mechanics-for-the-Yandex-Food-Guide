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
    let mapView = YMKMapView(frame: CGRect.zero)
    private lazy var map : YMKMap = {  return mapView?.mapWindow.map ?? .init() }()
    private let manager = CLLocationManager()
    private var targetPin: YMKPoint = .init()
    
    override init() {
        super.init()
        self.manager.delegate = self
        manager.requestAlwaysAuthorization()
        manager.startUpdatingLocation()
        
        map.isAwesomeModelsEnabled = true
    }
    
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        if status == .authorizedWhenInUse {
            self.manager.startUpdatingLocation()
        }
    }
    
    func placePins(_ models: [SnippetDTO]) {
        let iconStyle = YMKIconStyle()
        let image = UIImage(named: "pin") ?? UIImage()
        
        for model in models {
            let placemark = map.mapObjects.addPlacemark()
            placemark.geometry = .init(
                latitude: model.coordinates.lat,
                longitude: model.coordinates.lon
            )
            placemark.setIconWith(image, style: iconStyle)
        }
        
        guard let model = models.first else { return }
        targetPin = .init(
            latitude: model.coordinates.lat,
            longitude: model.coordinates.lon
        )
    }
    
    func centerCamera(to option: CameraTargetOption) {
        switch option {
        case .user:
            guard let userLocation = manager.location else { return }
            centerMapLocation(
                target: YMKPoint(
                    latitude: userLocation.coordinate.latitude,
                    longitude: userLocation.coordinate.longitude
                ),
                map: mapView ?? .init()
            )
        case .pins:
            centerMapLocation(
                target: targetPin,
                map: mapView ?? .init()
            )
        }
    }
    
    func getScreenPoints() -> (lowerLeftCorner: Point, topRightCorner: Point) {
        let lowerLeftScreenPoint = YMKScreenPoint(x: 0, y: Float(mapView?.mapWindow.height() ?? 0))
        let topRightScreenPoint = YMKScreenPoint(x: Float(mapView?.mapWindow.width() ?? 0), y: 0)
        
        let lowerLeftWorldPoint = mapView?.mapWindow.screenToWorld(with: lowerLeftScreenPoint) ?? YMKPoint()
        let topRightWorldPoint = mapView?.mapWindow.screenToWorld(with: topRightScreenPoint) ?? YMKPoint()
        
        return (
            lowerLeftCorner: .init(lat: lowerLeftWorldPoint.latitude, lon: lowerLeftWorldPoint.longitude),
            topRightCorner: .init(lat: topRightWorldPoint.latitude, lon: topRightWorldPoint.longitude)
        )
    }
    
    private func centerMapLocation(target location: YMKPoint?, map: YMKMapView) {
        guard let location = location else {
            print("Failed to get user location")
            return
        }
        
        map.mapWindow.map.move(
            with: YMKCameraPosition(target: location, zoom: 16, azimuth: 0, tilt: 0),
            animation: YMKAnimation(type: YMKAnimationType.smooth, duration: 0.5)
        )
    }
    
    enum CameraTargetOption {
        case user
        case pins
    }
}
