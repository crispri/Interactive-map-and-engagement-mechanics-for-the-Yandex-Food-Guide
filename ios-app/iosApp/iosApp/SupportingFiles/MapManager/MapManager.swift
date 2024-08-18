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
        map.setMapStyleWithStyle(StyleMap().styleMap)
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
        var cnt = 0
        print("-------------------")
        
        var bigPins = [SnippetDTO]()
        for i in pins.indices {
            if i == 0 {
                bigPins.append(pins[i])
                continue
            }
            var intersects = false
            var insideWhile = false
            var j = i + 1
            while j < pins.count {
                insideWhile = true
                if isIntersection((pins[i], .high), (pins[j], .high)) {
                    intersects = true
                    break
                }
                j += 1
            }
            if !intersects && insideWhile {
                bigPins.append(pins[i])
            }
        }
        print("big pins count: \(bigPins.count)")
        
        var normalPins = [SnippetDTO]()
        for  i in pins.indices {
            if !bigPins.contains(pins[i]) {
                var intersects = false
                var insideWhile = false
                
                for j in bigPins.indices {
                    insideWhile = true
                    if isIntersection((pins[i], .medium), (pins[j], .high)) {
                        intersects = true
                        break
                    }
                }
                
                for j in normalPins.indices {
                    insideWhile = true
                    if isIntersection((pins[i], .medium), (pins[j], .medium)) {
                        intersects = true
                        break
                    }
                }
                
                if !intersects && insideWhile {
                    normalPins.append(pins[i])
                }
            }
        }
        
        var smallPins = [SnippetDTO]()
        for  i in pins.indices {
            if !bigPins.contains(pins[i]) && !normalPins.contains(pins[i]) {
                smallPins.append(pins[i])
            }
        }
        
        
        let result = bigPins + normalPins + smallPins
        
        for index in result.indices {
            if var pp = placedPins[result[index].id] {
                pp.1 = true
                placedPins[result[index].id] = pp
            } else {
                let placemark = map.mapObjects.addPlacemark()
                placemark.geometry = .init(
                    latitude: result[index].coordinates.lat,
                    longitude: result[index].coordinates.lon
                )
                let commonStyle = YMKIconStyle(
                    anchor: CGPoint(x: 0.5, y: 1.0) as NSValue,
                    rotationType: .none,
                    zIndex: 1,
                    flat: false,
                    visible: true,
                    scale: 1.0,
                    tappableArea: nil
                )
                if index < bigPins.count {
                    let uiView = BigPinView(
                        frame: .init(x: 0, y: 0, width: 172, height: 106),
                        model: result[index]
                    )
                    uiView.setSelected(false)
                    placemark.setIconWith(uiView.asImage(), style: commonStyle)
                } else if index < bigPins.count + normalPins.count {
                    let uiView = NormalPinView(
                        frame: .init(x: 0, y: 0, width: PinSize.normal.width, height: PinSize.normal.height)
                    )
                    uiView.model = result[index]
                    uiView.setSelected(false)
                    placemark.setIconWith(uiView.asImage(), style: commonStyle)
                } else {
                    let smallStyle = YMKIconStyle(
                        anchor: CGPoint(x: 0.0, y: 0.0) as NSValue,
                        rotationType: .none,
                        zIndex: 0,
                        flat: false,
                        visible: true,
                        scale: 1.5,
                        tappableArea: nil
                    )
                    let uiView = SmallPinView(
                        frame: .init(x: 0, y: 0, width: PinSize.big.width, height: PinSize.big.height),
                        model: result[index]
                    )
                    placemark.setIconWith(uiView.asImage(), style: smallStyle)
                }
                placedPins[result[index].id] = (placemark, true)
                cnt += 1
            }
        }
        print("\(cnt) restaurants added;")
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
    
    private func getPriorityByIndex(_ index: Int) -> PinPriority {
        if index < 3 {
            return .high
        } else if index < 6 {
            return .medium
        } else if index < 20 {
            return .low
        } else {
            return .none
        }
    }
    
    private func isIntersection(_ lhs: (SnippetDTO, PinPriority), _ rhs: (SnippetDTO, PinPriority)) -> Bool {
        let lhsCenter = getCenter(lhs)
        let rhsCenter = getCenter(rhs)
        guard let coef = mapView?.mapWindow.scaleFactor else { return false }
        
        let result = (lhsCenter.x - rhsCenter.x) * (lhsCenter.x - rhsCenter.x) +
        (lhsCenter.y - rhsCenter.y) * (lhsCenter.y - rhsCenter.y) < Float(202 * 202) * (coef + 1) * (coef + 1)
        print(result)
        return result
    }
    
    private func getCenter(_ snippet: (SnippetDTO, PinPriority)) -> YMKScreenPoint {
        let originPoint = mapView?.mapWindow.worldToScreen(
            withWorldPoint: YMKPoint(
                latitude: snippet.0.coordinates.lat,
                longitude: snippet.0.coordinates.lon
            )
        ) ?? YMKScreenPoint()
        guard let coef = mapView?.mapWindow.scaleFactor else { return YMKScreenPoint() }
        switch snippet.1 {
        case .none:
            return .init(x: 0, y: 0)
        case .low:
            return .init(
                x: originPoint.x + Float(PinSize.small.width / 2) * coef,
                y: originPoint.y + Float(PinSize.small.height / 2) * coef
            )
        case .medium:
            return .init(
                x: originPoint.x,
                y: originPoint.y - Float(PinSize.normal.height / 2) * coef
            )
        case .high:
            return .init(
                x: originPoint.x,
                y: originPoint.y - Float(PinSize.big.height / 2) * coef
            )
        }
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
    
    enum PinPriority {
        case none
        case low
        case medium
        case high
    }
}
