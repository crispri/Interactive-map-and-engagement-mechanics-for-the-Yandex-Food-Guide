//
//  DetailsView.swift
//  iosApp
//
//  Created by Максим Кузнецов on 03.08.2024.
//

import SwiftUI

struct DetailsView: View {
    @Environment(\.dismiss) var dismiss
    @StateObject private var mapManager = MapManager()
    
    var body: some View {
        VStack {
            HStack {
                Button {
                    dismiss()
                } label: {
                    Image(systemName: "arrow.left")
                        .foregroundStyle(.black)
                        .padding()
                        .background(.white)
                        .clipShape(Circle())
                }
                Spacer()
                Button {
                    mapManager.currentUserLocation()
                } label: {
                    Image(systemName: "bookmark")
                        .foregroundStyle(.black)
                        .padding()
                        .background(.white)
                        .clipShape(Circle())
                }
            }
            .padding()
            Spacer()
        }
        .toolbar(.hidden, for: .navigationBar)
        .background {
            MapView()
                .environmentObject(mapManager)
        }
    }
}

#Preview {
    DetailsView()
}
