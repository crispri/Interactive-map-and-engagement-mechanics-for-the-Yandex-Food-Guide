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
        ScrollView {
            VStack(spacing: 0) {
                ForEach(viewModel.snippets, id: \.self) { snippet in
                    SnippetCell(restaurant: snippet, isEditUserCollectionsPresented: $isEditUserCollectionsPresented)
                        .onTapGesture {
                            selectedSnipped = snippet
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
            .background(
                Color.white
            )
        }
        .scrollTargetBehavior(.viewAligned)
    }
}
