//
//  BottomSheetView.swift
//  iosApp
//
//  Created by Максим Кузнецов on 04.08.2024.
//

import SwiftUI
import BottomSheet

struct BottomSheetView: View {
    @EnvironmentObject private var viewModel: SnippetViewModel
    @State private var isEditUserCollectionsPresented = false
    @State private var isSheetPresented = false
    @State private var selectedSnipped: SnippetDTO?
    
    var body: some View {
        if let selectedPin = viewModel.selectedPin, viewModel.isPinFocusMode {
            VStack {
                GrabberView()
                SnippetCell(
                    restaurant: Binding(get: {
                        selectedPin
                    }, set: { _ in }),
                    isEditUserCollectionsPresented: $isEditUserCollectionsPresented
                )
                .onTapGesture {
                    selectedSnipped = selectedPin
                    isSheetPresented = true
                }
                .sheet(item: $selectedSnipped, content: { item in
                    RestaurantView(restaurant: item)
                        .presentationCornerRadius(20)
                })
            }
            .background(
                Color.white
                    .clipShape(
                        RoundedCorner(
                            radius: 20,
                            corners: [
                                .topLeft,
                                .topRight
                            ]
                        )
                    )
            )
        } else {
            ScrollView {
                VStack(spacing: 0) {
                    ForEach($viewModel.snippets, id: \.self) { snippet in
                        SnippetCell(restaurant: snippet, isEditUserCollectionsPresented: $isEditUserCollectionsPresented)
                            .onTapGesture {
                                selectedSnipped = snippet.wrappedValue
                                isSheetPresented = true
                            }
                            .sheet(item: $selectedSnipped, content: { item in
                                RestaurantView(restaurant: item)
                            })
                    }
                }
                .padding(.bottom)
                .animation(.easeInOut, value: viewModel.snippets)
                .scrollTargetLayout()
                .background(Color.white)
            }
            .scrollTargetBehavior(.viewAligned)
        }
    }
}
