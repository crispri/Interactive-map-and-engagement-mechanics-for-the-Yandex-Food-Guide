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
    
    var body: some View {
        ScrollView {
            VStack(spacing: 0) {
                ForEach($viewModel.snippets, id: \.self) { snippet in
                    SnippetCell(restaurant: snippet, isEditUserCollectionsPresented: $isEditUserCollectionsPresented)
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
