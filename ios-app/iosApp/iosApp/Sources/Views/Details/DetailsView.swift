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
    @State var isFiltersPresented = false
    @State var isUserCollectionsPresented = false
    
    var body: some View {
        ZStack {
            YandexMapView()
                .edgesIgnoringSafeArea(.all)
                .environmentObject(viewModel.mapManager)
                .bottomSheet(
                    bottomSheetPosition: $viewModel.sheetPosition,
                    switchablePositions: [
                        .dynamicBottom,
                        .absolute(500),
                        .relativeTop(0.9)
                    ],
                    headerContent: { HeaderView(isFiltersPresented: $isFiltersPresented) },
                    mainContent: { BottomSheetView()  }
                )
                .enableAppleScrollBehavior(false)
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
        .sheet(isPresented: $isFiltersPresented) {
            FilterDetailedView()
        }
        .sheet(isPresented: $isUserCollectionsPresented) {
            UserCollectionsView()
        }
    }
    
    private var backButton: some View {
        Button {
            dismiss()
        } label: {
            Image("arrow-left")
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
            viewModel.onlyUserCollections.toggle()
            Task { await viewModel.fetchSelections() }
            viewModel.currentSelection = nil
            viewModel.eventCenterCamera(to: .pins)
        } label: {
            Image("Bookmark")
                .renderingMode(.template)
                .bold()
                .foregroundStyle(viewModel.onlyUserCollections ? .white : .black)
                .padding()
                .background(viewModel.onlyUserCollections ? .black : .white)
                .clipShape(Circle())
        }
        .shadow(radius: 20, x: 0, y: 8)
    }
}

#Preview {
    DetailsView()
        .environmentObject(SnippetViewModel())
}
