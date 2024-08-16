//
//  YandexMapView.swift
//  iosApp
//
//  Created by Максим Кузнецов on 03.08.2024.
//

import SwiftUI
import YandexMapsMobile

struct YandexMapView: UIViewRepresentable {
    @EnvironmentObject var locationManager : MapManager
    
    func makeUIView(context: Context) -> YMKMapView {
        return locationManager.mapView ?? .init()
    }
         
    func updateUIView(_ mapView: YMKMapView, context: Context) {}
}
