//
//  DetailsView.swift
//  iosApp
//
//  Created by Максим Кузнецов on 03.08.2024.
//

import SwiftUI
import BottomSheet

struct DetailsView: View {
    @Environment(\.dismiss) var dismiss
    @EnvironmentObject private var viewModel: SnippetViewModel
    @State private var isBottomSheetPresented = true
    @State var sheetPosition: BottomSheetPosition = .dynamicBottom
    
    var body: some View {
        ZStack {
            YandexMapView()
                .edgesIgnoringSafeArea(.all)
                .environmentObject(viewModel.mapManager)
                .bottomSheet(
                    bottomSheetPosition: $sheetPosition,
                    switchablePositions: [
                        .dynamicBottom,
                        .absolute(500),
                        .relativeTop(0.9)
                    ],
                    headerContent: { HeaderView() },
                    mainContent: { BottomSheetView()  }
                )
                .enableAppleScrollBehavior(true)
                .showDragIndicator(false)
                .customBackground(
                    VStack(spacing: 0) {
                        Color.clear
                            .frame(height: 150)
                        Color.white
                    }.edgesIgnoringSafeArea(.all)
                )
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
        .toolbar(.hidden, for: .navigationBar)
        .onAppear {
            viewModel.eventOnAppear()
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
            viewModel.eventCenterCamera(to: .user)
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
