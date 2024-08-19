//
//  MapObjectTapListener.swift
//  iosApp
//
//  Created by Максим Кузнецов on 19.08.2024.
//

import Foundation
import YandexMapsMobile

final class MapObjectTapListener: NSObject, YMKMapObjectTapListener {
    private weak var mapManager: MapManager?
    private weak var viewModel: SnippetViewModel?
    
    init(mapManager: MapManager?, viewModel: SnippetViewModel?) {
        self.mapManager = mapManager
        self.viewModel = viewModel
    }
    
    @MainActor 
    func onMapObjectTap(with mapObject: YMKMapObject, point: YMKPoint) -> Bool {
        print("Tapped point \((point.latitude, point.longitude))")
        if let placemark = mapObject as? YMKPlacemarkMapObject {
            mapManager?.centerCamera(to: placemark.geometry)
            viewModel?.eventOpenBottomSheet()
        }
        return true
    }
}
