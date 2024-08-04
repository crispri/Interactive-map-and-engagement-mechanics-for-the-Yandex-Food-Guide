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
    @State private var userLocaitonTitle = "2-я Брестская, 1/5"
    
    var body: some View {
        VStack {
            HStack {
                backButton
                Spacer()
                userLocationButton
                Spacer()
                bookmarkButton
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
    
    private var backButton: some View {
        Button {
            dismiss()
        } label: {
            Image(systemName: "arrow.left")
                .bold()
                .foregroundStyle(.black)
                .padding()
                .background(.white)
                .clipShape(Circle())
        }
        .shadow(radius: 20, x: 0, y: 8)
    }
    
    private var userLocationButton: some View {
        Button {
            
            mapManager.currentUserLocation()
        } label: {
            VStack(spacing: 0) {
                HStack(spacing: 4) {
                    Text("Ваше местоположение")
                        .font(.system(size: 13))
                        .foregroundStyle(.black)
                    Image(systemName: "chevron.right")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 10, height: 10)
                        .foregroundStyle(.black)
                }
                Text(userLocaitonTitle)
                    .font(.system(size: 16, weight: .medium))
                    .foregroundStyle(.black)
            }
        }
    }
    
    private var bookmarkButton: some View {
        Button {
            // TODO: add action.
        } label: {
            Image(systemName: "bookmark")
                .bold()
                .foregroundStyle(.black)
                .padding()
                .background(.white)
                .clipShape(Circle())
        }
        .shadow(radius: 20, x: 0, y: 8)
    }
}

#Preview {
    DetailsView()
}
