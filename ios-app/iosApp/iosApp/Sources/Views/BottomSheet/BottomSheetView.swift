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
    @Binding var sheetDetents: SheetDetents
    
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
                }
                .scrollTargetBehavior(.viewAligned)
                .padding(.bottom, sheetDetents.getPadding())
            }
            .background {
                Color.white
                    .clipShape(RoundedRectangle(cornerRadius: 20))
            }
        }
    }
}
