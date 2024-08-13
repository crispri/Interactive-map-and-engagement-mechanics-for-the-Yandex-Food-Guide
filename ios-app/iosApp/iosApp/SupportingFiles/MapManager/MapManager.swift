//
//  MapManager.swift
//  iosApp
//
//  Created by Максим Кузнецов on 03.08.2024.
//

import Foundation
import CoreLocation
import YandexMapsMobile

final class MapManager: NSObject, CLLocationManagerDelegate, ObservableObject {
    let mapView = YMKMapView(frame: CGRect.zero)
    private lazy var map : YMKMap = {  return mapView?.mapWindow.map ?? .init() }()
    private let manager = CLLocationManager()
    private var targetPin: YMKPoint = .init()
    private let cameraListener = CameraListener()
    var delegate: SnippetViewModel? = nil
    private var placedPins: [String: (YMKPlacemarkMapObject, Bool)] = [:]
    
    override init() {
        super.init()
        self.cameraListener.delegate = self
        map.addCameraListener(with: cameraListener)
        
        self.manager.delegate = self
        manager.requestAlwaysAuthorization()
        manager.startUpdatingLocation()
        
        map.isAwesomeModelsEnabled = true
    }
    
    @MainActor 
    func eventOnGesture() {
        delegate?.eventOnGesture()
    }
    
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        if status == .authorizedWhenInUse {
            self.manager.startUpdatingLocation()
        }
    }
    
    func placePins(_ pins: [SnippetDTO]) {
        let iconStyle = YMKIconStyle()
        let image = UIImage(named: "pin") ?? UIImage()
        
        var cnt = 0;
        
        disablePins()
        
        for pin in pins {
            if var pp = placedPins[pin.id] {
                pp.1 = true
                placedPins[pin.id] = pp
            } else {
                let placemark = map.mapObjects.addPlacemark()
                placemark.geometry = .init(
                    latitude: pin.coordinates.lat,
                    longitude: pin.coordinates.lon
                )
                placemark.setIconWith(image, style: iconStyle)
                placedPins[pin.id] = (placemark, true)
                cnt += 1
            }
        }
        print("\(cnt) restaurants added;")
        
        cleanPins()
        
        guard let model = pins.first else { return }
        targetPin = .init(
            latitude: model.coordinates.lat,
            longitude: model.coordinates.lon
        )
    }
    
    func disablePins() {
        for kv in placedPins {
            placedPins[kv.key] = (kv.value.0, false)
        }
    }
    
    func cleanPins() {
        for kv in placedPins {
            if !kv.value.1 {
                map.mapObjects.remove(with: kv.value.0)
                placedPins.removeValue(forKey: kv.key)
            }
        }
    }
    
    func placeUser() {
        guard let userLocation = manager.location else { return }
        let iconStyle = YMKIconStyle()
        let image = UIImage(named: "me") ?? UIImage()
        
        let placemark = map.mapObjects.addPlacemark()
        placemark.geometry = .init(
            latitude: userLocation.coordinate.latitude,
            longitude: userLocation.coordinate.longitude
        )
        placemark.setIconWith(image, style: iconStyle)
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
                zoom: 12,
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
    
    private func centerMapLocation(target location: YMKPoint?, zoom: Int = 16, map: YMKMapView) {
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
