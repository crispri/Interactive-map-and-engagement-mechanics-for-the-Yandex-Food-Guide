//
//  CameraListener.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 09.08.2024.
//

import Foundation
import YandexMapsMobile

final class CameraListener: NSObject, YMKMapCameraListener {
    var delegate: MapManager?
    private var lastRequestTime: Date?
    private let throttleInterval: TimeInterval = 0.5

    @MainActor
    func onCameraPositionChanged(with map: YMKMap, cameraPosition: YMKCameraPosition, cameraUpdateReason: YMKCameraUpdateReason, finished: Bool) {
        guard !(delegate?.isPinFocusMode ?? false) else { return }
        guard let delegate else {
            print("No delegate")
            return
        }
        guard finished else { return }
        
        delegate.disablePins()
        delegate.cleanPins()

        let now = Date()

        if let lastTime = lastRequestTime {
            if now.timeIntervalSince(lastTime) < throttleInterval {
                return
            }
        }

        lastRequestTime = now

        Task { @MainActor in
            print("...restaurant request")
            delegate.eventOnGesture()
        }
    }
}
