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
    
    var body: some View {
        VStack(spacing: 0) {
            ForEach(viewModel.snippets, id: \.self) { snippet in
                SnippetCell(restaurant: snippet)
            }
        }
        .animation(.easeInOut, value: viewModel.snippets)
        .background(
            Color.white
        )
//        GeometryReader { geometry in
//            ScrollView {
//                
//            }
//            .frame(height: UIScreen.main.bounds.height * 0.7)
//            .scrollTargetBehavior(.viewAligned)
//            .background(
//                Color.white
//            )
//        }
    }
}
