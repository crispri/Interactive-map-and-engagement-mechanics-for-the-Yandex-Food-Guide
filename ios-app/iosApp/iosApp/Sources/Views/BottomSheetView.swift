//
//  BottomSheetView.swift
//  iosApp
//
//  Created by Максим Кузнецов on 04.08.2024.
//

import SwiftUI

struct BottomSheetView: View {
    @EnvironmentObject private var viewModel: SnippetViewModel
    
    var body: some View {
        VStack {
            FilterView()
                .padding(.top, 16)
            ScrollView {
                VStack(spacing: 0) {
                    ForEach(viewModel.snippets, id: \.self) { snippet in
                        SnippetCell(restaurant: snippet)
                    }
                }
                .scrollTargetLayout()
            }
            .scrollTargetBehavior(.paging)
        }
    }
}

#Preview {
    DetailsView()
        .environmentObject(SnippetViewModel())
}
