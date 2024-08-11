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
    @Binding var sheetPosition: BottomSheetPosition
    @State var bottomPadding: CGFloat = 0
    
    var body: some View {
        VStack {
            SelectionScrollView()
                .environmentObject(viewModel)
            VStack {
                Capsule()
                    .fill(Color.secondary)
                    .opacity(0.5)
                    .frame(width: 35, height: 5)
                    .padding(6)
                FilterView()
                ScrollView {
                    VStack(spacing: 0) {
                        ForEach(viewModel.snippets, id: \.self) { snippet in
                            SnippetCell(restaurant: snippet)
                        }
                    }
                    .scrollTargetLayout()
                    .animation(.spring, value: viewModel.snippets)
                }
                .scrollTargetBehavior(.viewAligned)
            }
            .background {
                RoundedRectangle(cornerRadius: 20)
                    .fill(.white)
                    .frame(minHeight: 600)
            }
        }
    }
}
