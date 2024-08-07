//
//  DetailsView.swift
//  iosApp
//
//  Created by Максим Кузнецов on 03.08.2024.
//

import SwiftUI

struct DetailsView: View {
    @Environment(\.dismiss) var dismiss
    @EnvironmentObject private var viewModel: SnippetViewModel
    @State private var isBottomSheetPresented = true
    
    var body: some View {
        VStack {
            MapView()
                .environmentObject(viewModel.mapManager)
        }
        .toolbar(.hidden, for: .navigationBar)
        .overlay {
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
        }
        .onAppear {
            viewModel.eventOnAppear()
        }
        .sheet(isPresented: $isBottomSheetPresented) {
            BottomSheetView()
                .presentationDetents([.fraction(0.15), .medium,])
                .presentationDragIndicator(.visible)
                .presentationBackgroundInteraction(.enabled)
                .interactiveDismissDisabled()
                .presentationCornerRadius(40)
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
            viewModel.eventFetchUserLocation()
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
                Text(viewModel.userLocaitonTitle)
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
        .environmentObject(SnippetViewModel())
}
