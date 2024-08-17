//
//  MapManager.swift
//  iosApp
//
//  Created by Максим Кузнецов on 03.08.2024.
//

import Foundation
import CoreLocation
import YandexMapsMobile
import SwiftUI

@MainActor
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
        
        map.isRotateGesturesEnabled = false
    }
    
    func eventOnGesture() {
        delegate?.onCameraMove()
    }
    
    nonisolated func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        if status == .authorizedWhenInUse {
            self.manager.startUpdatingLocation()
        }
    }
    
    func placePins(_ pins: [SnippetDTO]) {
        var cnt = 0;
        
        disablePins()
        
        for index in pins.indices {
            if var pp = placedPins[pins[index].id] {
                pp.1 = true
                placedPins[pins[index].id] = pp
            } else {
                let placemark = map.mapObjects.addPlacemark()
                placemark.geometry = .init(
                    latitude: pins[index].coordinates.lat,
                    longitude: pins[index].coordinates.lon
                )
                let commonStyle = YMKIconStyle(
                    anchor: CGPoint(x: 0.5, y: 1.0) as NSValue,
                    rotationType: .none,
                    zIndex: 0,
                    flat: true,
                    visible: true,
                    scale: 1.0,
                    tappableArea: nil
                )
                if index < 3 {
                    let uiView = BigPinView(frame: .init(x: 0, y: 0, width: 172, height: 106))
                    uiView.model = pins[index]
                    uiView.setSelected(false)
                    placemark.setIconWith(uiView.asImage(), style: commonStyle)
                } else if index < 6 {
                    let uiView = NormalPinView(frame: .init(x: 0, y: 0, width: 172, height: 52))
                    uiView.model = pins[index]
                    uiView.setSelected(false)
                    placemark.setIconWith(uiView.asImage(), style: commonStyle)
                } else if index < 20 {
                    let smallStyle = YMKIconStyle(
                        anchor: CGPoint(x: 0.5, y: 0.5) as NSValue,
                        rotationType: .none,
                        zIndex: 0,
                        flat: true,
                        visible: true,
                        scale: 2.0,
                        tappableArea: nil
                    )
                    let uiView = SmallPinView(frame: .init(x: 0, y: 0, width: 7, height: 10))
                    uiView.setSelected(false)
                    placemark.setIconWith(uiView.asImage(), style: smallStyle)
                }
                placedPins[pins[index].id] = (placemark, true)
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
            let centerCity = YMKPoint(
                latitude: 55.749956,
                longitude:  37.615812
            )
            centerMapLocation(
                target: centerCity,
                zoom: 12,
                map: mapView ?? .init()
            )
        }
    }
    
    func getScreenPoints() -> (lowerLeftCorner: Point, topRightCorner: Point) {
        let lowerLeftScreenPoint = YMKScreenPoint(
            x: 0,
            y: Float(mapView?.mapWindow.height() ?? 0) * 0.66
        )
        let topRightScreenPoint = YMKScreenPoint(
            x: Float(mapView?.mapWindow.width() ?? 0),
            y: 0
        )
        
        let lowerLeftWorldPoint = mapView?.mapWindow.screenToWorld(with: lowerLeftScreenPoint) ?? YMKPoint()
        let topRightWorldPoint = mapView?.mapWindow.screenToWorld(with: topRightScreenPoint) ?? YMKPoint()
        
        return (
            lowerLeftCorner: .init(lat: lowerLeftWorldPoint.latitude, lon: lowerLeftWorldPoint.longitude),
            topRightCorner: .init(lat: topRightWorldPoint.latitude, lon: topRightWorldPoint.longitude)
        )
    }
    
    private func centerMapLocation(target location: YMKPoint?, zoom: Float = 16, map: YMKMapView) {
        guard let location = location else {
            print("Failed to get user location")
            return
        }
        
        map.mapWindow.map.move(
            with: YMKCameraPosition(target: location, zoom: zoom, azimuth: 0, tilt: 0),
            animation: YMKAnimation(type: YMKAnimationType.smooth, duration: 0.5)
        )
    }
    
    func getUserLocation() throws -> Point {
        guard let userLocation = manager.location else {
            throw CLError(.locationUnknown)
        }
        return Point(lat: userLocation.coordinate.latitude, lon: userLocation.coordinate.longitude)
    }
    
    enum CameraTargetOption {
        case user
        case pins
    }
}
