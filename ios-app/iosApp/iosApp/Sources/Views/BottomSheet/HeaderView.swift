//
//  HeaderView.swift
//  iosApp
//
//  Created by Максим Кузнецов on 13.08.2024.
//

import SwiftUI

struct HeaderView: View {
    @EnvironmentObject private var viewModel: SnippetViewModel
    @Binding var isFiltersPresented: Bool
    
    var body: some View {
        VStack {
            if viewModel.isPinFocusMode {
                Spacer().frame(height: 100)
            } else {
                SelectionScrollView().environmentObject(viewModel)
            }
            VStack {
                if !viewModel.isPinFocusMode {
                    GrabberView()
                    FilterView(isFiltersPresented: $isFiltersPresented, filterCategories: $viewModel.filterCategories)
                        .padding(.bottom, 8)
                }
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
        }
    }
}
