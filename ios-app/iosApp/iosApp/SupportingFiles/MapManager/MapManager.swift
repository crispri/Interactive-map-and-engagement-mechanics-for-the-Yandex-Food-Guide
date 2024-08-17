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
        
        var snippets = pins
        sortSnippets(&snippets)
        for index in snippets.indices {
            if var pp = placedPins[snippets[index].id] {
                pp.1 = true
                placedPins[snippets[index].id] = pp
            } else {
                let placemark = map.mapObjects.addPlacemark()
                placemark.geometry = .init(
                    latitude: snippets[index].coordinates.lat,
                    longitude: snippets[index].coordinates.lon
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
                    let uiView = BigPinView(
                        frame: .init(x: 0, y: 0, width: PinSize.big.width, height: PinSize.big.height)
                    )
                    uiView.model = snippets[index]
                    uiView.setSelected(false)
                    placemark.setIconWith(uiView.asImage(), style: commonStyle)
                } else if index < 6 {
                    let uiView = NormalPinView(
                        frame: .init(x: 0, y: 0, width: PinSize.normal.width, height: PinSize.normal.height)
                    )
                    uiView.model = snippets[index]
                    uiView.setSelected(false)
                    placemark.setIconWith(uiView.asImage(), style: commonStyle)
                } else if index < 14 {
                    let smallStyle = YMKIconStyle(
                        anchor: CGPoint(x: 0.5, y: 0.5) as NSValue,
                        rotationType: .none,
                        zIndex: 0,
                        flat: true,
                        visible: true,
                        scale: 2.0,
                        tappableArea: nil
                    )
                    let uiView = SmallPinView(
                        frame: .init(x: 0, y: 0, width: PinSize.big.width, height: PinSize.big.height)
                    )
                    uiView.setSelected(false)
                    placemark.setIconWith(uiView.asImage(), style: smallStyle)
                }
                placedPins[snippets[index].id] = (placemark, true)
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
    
    private func sortSnippets(_ snippets: inout [SnippetDTO]) {
        var index = 0
        
        while index < snippets.count {
            var j = 0
            var hasOverlap = false
            
            while j < index {
                if isIntersection((snippets[index], getPriorityByIndex(index)), (snippets[j], getPriorityByIndex(index))) {
                    hasOverlap = true
                    break
                }
                j += 1
            }
            
            if hasOverlap {
                let overlappingRect = snippets.remove(at: index)
                snippets.append(overlappingRect)
            } else {
                index += 1
            }
        }
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
        let lhsRect = getRect(lhs)
        let rhsRect = getRect(rhs)
        
        if lhsRect.rightBottomX < rhsRect.leftTopX || rhsRect.rightBottomX < lhsRect.leftTopX {
            return false
        }
        
        if lhsRect.rightBottomY > rhsRect.leftTopY || rhsRect.rightBottomY > lhsRect.leftTopY {
            return false
        }
        
        return true
    }
    
    private func getRect(_ snippet: (SnippetDTO, PinPriority)) -> Rectangle {
        let lhsCenter = mapView?.mapWindow.worldToScreen(
            withWorldPoint: YMKPoint(
                latitude: snippet.0.coordinates.lat,
                longitude: snippet.0.coordinates.lon
            )
        ) ?? YMKScreenPoint()
        var lhsTopLeft: YMKScreenPoint? = nil
        var lhsLowerRight: YMKScreenPoint? = nil
        switch snippet.1 {
        case .none:
            return .init(leftTopX: 0, leftTopY: 0, rightBottomX: 0, rightBottomY: 0)
        case .low:
            lhsTopLeft = .init(
                x: lhsCenter.x - Float(PinSize.small.width / 2),
                y: lhsCenter.y - Float(PinSize.small.height / 2)
            )
            lhsLowerRight = .init(
                x: lhsCenter.x + Float(PinSize.small.width / 2),
                y: lhsCenter.y + Float(PinSize.small.height / 2)
            )
        case .medium:
            lhsTopLeft = .init(
                x: lhsCenter.x - Float(PinSize.normal.width / 2),
                y: lhsCenter.y - Float(PinSize.normal.height)
            )
            lhsLowerRight = .init(
                x: lhsCenter.x + Float(PinSize.normal.width / 2),
                y: lhsCenter.y
            )
        case .high:
            lhsTopLeft = .init(
                x: lhsCenter.x - Float(PinSize.big.width / 2),
                y: lhsCenter.y - Float(PinSize.big.height)
            )
            lhsLowerRight = .init(
                x: lhsCenter.x + Float(PinSize.big.width / 2),
                y: lhsCenter.y
            )
        }
        guard let lhsTopLeft, let lhsLowerRight else {
            return .init(leftTopX: 0, leftTopY: 0, rightBottomX: 0, rightBottomY: 0)
        }
        return Rectangle(
            leftTopX: lhsTopLeft.x,
            leftTopY: lhsTopLeft.y,
            rightBottomX: lhsLowerRight.x,
            rightBottomY: lhsLowerRight.y
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
    
    enum PinPriority {
        case none
        case low
        case medium
        case high
    }
    
    struct Rectangle {
        var leftTopX: Float
        var leftTopY: Float
        var rightBottomX: Float
        var rightBottomY: Float
    }
}
