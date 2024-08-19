//
//  InputListener.swift
//  iosApp
//
//  Created by Максим Кузнецов on 19.08.2024.
//

import Foundation
import YandexMapsMobile

final class InputListener: NSObject, YMKMapInputListener {
    private weak var mapManager: MapManager?
    
    init(mapManager: MapManager?) {
        self.mapManager = mapManager
    }
    
    @MainActor
    func onMapTap(with map: YMKMap, point: YMKPoint) {
        mapManager?.isPinFocusMode = false
        mapManager?.disablePins()
        mapManager?.cleanPins()
        mapManager?.eventOnGesture()
    }

    func onMapLongTap(with map: YMKMap, point: YMKPoint) {
        // Handle long tap
    }
}
