//
//  InputListener.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 09.08.2024.
//

import Foundation
import YandexMapsMobile

final class CameraListener: NSObject, YMKMapCameraListener {
    var delegate: MapManager?
    
    // TODO: Уменьшить количество запросов в секунду
    @MainActor
    func onCameraPositionChanged(with map: YMKMap, cameraPosition: YMKCameraPosition, cameraUpdateReason: YMKCameraUpdateReason, finished: Bool) {
        if let delegate {
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                delegate.eventOnGesture()
            }
        } else {
            print("No delegate")
        }
    }
}
