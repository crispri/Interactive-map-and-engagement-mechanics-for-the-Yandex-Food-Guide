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
    lazy var map : YMKMap = {  return mapView?.mapWindow.map ?? .init() }()
    private let manager = CLLocationManager()
    private let cameraListener = CameraListener()
    private var delegate: SnippetViewModel?
    private var placedPins: [String: (YMKPlacemarkMapObject, Bool)] = [:]
    private var userPin: YMKPlacemarkMapObject? = nil
    private var mapObjectTapListener: YMKMapObjectTapListener?
    private var inputListener: InputListener!
    var isPinFocusMode = false {
        didSet {
            delegate?.pinFocusModeEnabled(isPinFocusMode)
        }
    }
    
    init(delegate: SnippetViewModel?) {
        super.init()
        self.delegate = delegate
        manager.delegate = self
        manager.requestAlwaysAuthorization()
        manager.startUpdatingLocation()
        map.isRotateGesturesEnabled = false
        map.setMapStyleWithStyle(StyleMap().styleMap)
        
        self.cameraListener.delegate = self
        map.addCameraListener(with: cameraListener)
        
        inputListener = InputListener(mapManager: self)
        map.addInputListener(with: inputListener)
        
        mapObjectTapListener = MapObjectTapListener(mapManager: self, viewModel: delegate)
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
        
        var bigPins = [SnippetDTO]()
        for i in pins.indices {
            if i >= 3 { break }
            if i == 0 {
                bigPins.append(pins[i])
                continue
            }
            var intersects = false
            
            let currentOrigin = mapView?.mapWindow.worldToScreen(
                withWorldPoint: YMKPoint(
                    latitude: pins[i].coordinates.lat,
                    longitude: pins[i].coordinates.lon
                )
            ) ?? YMKScreenPoint()
            let currentRect = getRectByOrigin(currentOrigin, with: .high)
            
            for j in bigPins.indices {
                let originToCompare = mapView?.mapWindow.worldToScreen(
                    withWorldPoint: YMKPoint(
                        latitude: pins[j].coordinates.lat,
                        longitude: pins[j].coordinates.lon
                    )
                ) ?? YMKScreenPoint()
                let rectToCompare = getRectByOrigin(originToCompare, with: .high)
                if currentRect.intersects(rectToCompare) {
                    intersects = true
                    break
                }
            }
            if !intersects {
                bigPins.append(pins[i])
            }
        }
        
        var normalPins = [SnippetDTO]()
        for i in pins.indices {
            if i >= 6 { break }
            if bigPins.contains(pins[i]) { continue }
            var intersects = false
            
            let currentOrigin = mapView?.mapWindow.worldToScreen(
                withWorldPoint: YMKPoint(
                    latitude: pins[i].coordinates.lat,
                    longitude: pins[i].coordinates.lon
                )
            ) ?? YMKScreenPoint()
            let currentRect = getRectByOrigin(currentOrigin, with: .medium)
            
            for j in bigPins.indices {
                let originToCompare = mapView?.mapWindow.worldToScreen(
                    withWorldPoint: YMKPoint(
                        latitude: pins[j].coordinates.lat,
                        longitude: pins[j].coordinates.lon
                    )
                ) ?? YMKScreenPoint()
                let rectToCompare = getRectByOrigin(originToCompare, with: .high)
                if currentRect.intersects(rectToCompare) {
                    intersects = true
                    break
                }
            }
            
            for j in normalPins.indices {
                let originToCompare = mapView?.mapWindow.worldToScreen(
                    withWorldPoint: YMKPoint(
                        latitude: pins[j].coordinates.lat,
                        longitude: pins[j].coordinates.lon
                    )
                ) ?? YMKScreenPoint()
                let rectToCompare = getRectByOrigin(originToCompare, with: .medium)
                if currentRect.intersects(rectToCompare) {
                    intersects = true
                    break
                }
            }
            
            if !intersects {
                normalPins.append(pins[i])
            }
        }
        
        var smallPins = [SnippetDTO]()
        for  i in pins.indices {
            if !bigPins.contains(pins[i]) && !normalPins.contains(pins[i]) {
                var intersects = false
                
                let currentOrigin = mapView?.mapWindow.worldToScreen(
                    withWorldPoint: YMKPoint(
                        latitude: pins[i].coordinates.lat,
                        longitude: pins[i].coordinates.lon
                    )
                ) ?? YMKScreenPoint()
                let currentRect = getRectByOrigin(currentOrigin, with: .low)
                
                for j in smallPins.indices {
                    let originToCompare = mapView?.mapWindow.worldToScreen(
                        withWorldPoint: YMKPoint(
                            latitude: pins[j].coordinates.lat,
                            longitude: pins[j].coordinates.lon
                        )
                    ) ?? YMKScreenPoint()
                    let rectToCompare = getRectByOrigin(originToCompare, with: .low)
                    if currentRect.intersects(rectToCompare) {
                        intersects = true
                        break
                    }
                }
                
                if !intersects {
                    smallPins.append(pins[i])
                }
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
                        frame: .init(x: 0, y: 0, width: PinSize.big.width, height: PinSize.big.height),
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
                        anchor: CGPoint(x: 0.5, y: 0.5) as NSValue,
                        rotationType: .none,
                        zIndex: 0,
                        flat: false,
                        visible: true,
                        scale: 1.5,
                        tappableArea: nil
                    )
                    let uiView = SmallPinView(
                        frame: .init(x: 0, y: 0, width: PinSize.small.width, height: PinSize.small.height),
                        model: result[index]
                    )
                    placemark.setIconWith(uiView.asImage(), style: smallStyle)
                }
                placedPins[result[index].id] = (placemark, true)
                cnt += 1
                
                if let mapObjectTapListener {
                    placemark.addTapListener(with: mapObjectTapListener)
                }
                placemark.userData = result[index]
            }
        }
        print("\(cnt) restaurants added;")
    }
    
    private func getRectByOrigin(_ originPoint: YMKScreenPoint, with priority: PinPriority) -> CGRect {
        guard let coef = mapView?.mapWindow.scaleFactor else { return .init() }
        
        var x = CGFloat(originPoint.x)
        var y = CGFloat(originPoint.y)
        var width = CGFloat(coef) * PinSize.small.width
        var height = CGFloat(coef) * PinSize.small.height
        switch priority {
        case .low:
            x -= CGFloat(coef) * PinSize.normal.width / 2
            y -= CGFloat(coef) * PinSize.normal.height / 2
        case .medium:
            x -= CGFloat(coef) * PinSize.normal.width / 2
            y -= CGFloat(coef) * PinSize.normal.height
            width = CGFloat(coef) * PinSize.normal.width
            height = CGFloat(coef) * PinSize.normal.height
        case .high:
            x -= CGFloat(coef) * PinSize.big.width / 2
            y -= CGFloat(coef) * PinSize.big.height
            width = CGFloat(coef) * PinSize.big.width
            height = CGFloat(coef) * PinSize.big.height
        }
        
        return CGRect(x: Int(x), y: Int(y), width: Int(width), height: Int(height))
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
        
        if let userPin {
            map.mapObjects.remove(with: userPin)
        }
        
        let placemark = map.mapObjects.addPlacemark()
        placemark.geometry = .init(
            latitude: userLocation.coordinate.latitude,
            longitude: userLocation.coordinate.longitude
        )
        placemark.setIconWith(image, style: iconStyle)
        userPin = placemark
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
    
    func eventPinPressed(to placemark: YMKPlacemarkMapObject) {
        isPinFocusMode = true
        centerMapLocation(
            target: placemark.geometry,
            map: mapView ?? .init()
        )
        
        let smallPinStyle = YMKIconStyle(
            anchor: CGPoint(x: 0.5, y: 0.5) as NSValue,
            rotationType: .none,
            zIndex: 0,
            flat: false,
            visible: true,
            scale: 1.5,
            tappableArea: nil
        )
        for kv in placedPins {
            if kv.value.0 != placemark,
               let userData = placemark.userData as? SnippetDTO {
                let uiView = SmallPinView(
                    frame: .init(x: 0, y: 0, width: PinSize.small.width, height: PinSize.small.height),
                    model: userData
                )
                kv.value.0.setIconWith(uiView.asImage(), style: smallPinStyle)
            }
        }
        
        let bigPinStyle = YMKIconStyle(
            anchor: CGPoint(x: 0.5, y: 1.0) as NSValue,
            rotationType: .none,
            zIndex: 1,
            flat: false,
            visible: true,
            scale: 1.0,
            tappableArea: nil
        )
        guard let userData = placemark.userData as? SnippetDTO else { return }
        let uiView = BigPinView(
            frame: .init(x: 0, y: 0, width: PinSize.big.width, height: PinSize.big.height),
            model: userData
        )
        uiView.setSelected(true)
        placemark.setIconWith(uiView.asImage(), style: bigPinStyle)
        delegate?.selectedPin = userData
    }
    
    func eventSnippetAppeared(_ snippet: SnippetDTO) {
        let smallPinStyle = YMKIconStyle(
            anchor: CGPoint(x: 0.5, y: 0.5) as NSValue,
            rotationType: .none,
            zIndex: 0,
            flat: false,
            visible: true,
            scale: 1.5,
            tappableArea: nil
        )
        for kv in placedPins {
            if kv.key != snippet.id,
               let userData = kv.value.0.userData as? SnippetDTO {
                let uiView = SmallPinView(
                    frame: .init(x: 0, y: 0, width: PinSize.small.width, height: PinSize.small.height),
                    model: userData
                )
                kv.value.0.setIconWith(uiView.asImage(), style: smallPinStyle)
            }
        }
        
        let bigPinStyle = YMKIconStyle(
            anchor: CGPoint(x: 0.5, y: 1.0) as NSValue,
            rotationType: .none,
            zIndex: 1,
            flat: false,
            visible: true,
            scale: 1.0,
            tappableArea: nil
        )
        let uiView = BigPinView(
            frame: .init(x: 0, y: 0, width: PinSize.big.width, height: PinSize.big.height),
            model: snippet
        )
        uiView.setSelected(true)
        placedPins[snippet.id]?.0.setIconWith(uiView.asImage(), style: bigPinStyle)
    }
    
    func getScreenPoints(sheetPosition: SheetPosition = .bottom) -> (lowerLeftCorner: Point, topRightCorner: Point) {
        var coef: Float = 1.0
        switch sheetPosition {
        case .bottom:
            coef = 0.8
        case .medium:
            coef = 0.5
        }
        let lowerLeftScreenPoint = YMKScreenPoint(
            x: 0,
            y: Float(mapView?.mapWindow.height() ?? 0) * coef
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
    
    private func getPinSizeByIndex(_ index: Int) -> CGSize {
        if index < 3 {
            return PinSize.big
        } else if index < 6 {
            return PinSize.normal
        }
        return PinSize.small
    }
    
    func getUserLocation() throws -> Point {
        guard let userLocation = manager.location else {
            throw CLError(.locationUnknown)
        }
        return Point(lat: userLocation.coordinate.latitude, lon: userLocation.coordinate.longitude)
    }
}

extension MapManager {
    enum CameraTargetOption {
        case user
        case pins
    }
    
    enum SheetPosition {
        case bottom
        case medium
    }
    
    enum PinPriority {
        case low
        case medium
        case high
    }
}
