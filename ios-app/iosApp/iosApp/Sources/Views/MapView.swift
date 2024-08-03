//
//  MapView.swift
//  iosApp
//
//  Created by Максим Кузнецов on 03.08.2024.
//

import SwiftUI

struct MapView: View {
    @StateObject var mapManager = MapManager()
    var body: some View {
        ZStack{
            YandexMapView()
                .edgesIgnoringSafeArea(.all)
                .environmentObject(mapManager)
        }.onAppear{
            mapManager.currentUserLocation()
        }
    }
}

#Preview {
    MapView()
}
