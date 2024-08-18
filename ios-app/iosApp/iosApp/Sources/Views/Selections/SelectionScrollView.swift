//
//  SelectionStackView.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 03.08.2024.
//

import SwiftUI

struct SelectionScrollView: View {
    @EnvironmentObject var viewModel: SnippetViewModel
    
    var body: some View {
        ScrollViewReader { reader in
            ScrollView(.horizontal) {
                LazyHStack(alignment: .bottom) {
                    ForEach(viewModel.selections.indices, id: \.self) { index in
                        SelectionView(
                            title: viewModel.selections[index].name,
                            desc: viewModel.selections[index].description,
                            imageUrlString: viewModel.selections[index].picture,
                            selected: Binding(
                                get: {
                                    if let selectedCollection = viewModel.selectedCollection,
                                       selectedCollection == viewModel.selections[index] {
                                        return true
                                    }
                                    return false
                                },
                                set: { _ in }
                            )
                        ) {
                            Task {
                                await viewModel.eventSelectionPressed(at: index, reader: reader)
                            }
                        } bookmarkAction: {
                            // TODO: add bookmarkAction.
                        } infoAction: {
                            // TODO: add bookmarkAction.
                        }
                        .id(index)
                    }
                }
                .padding(.horizontal)
                .animation(.spring(duration: 0.2), value:  viewModel.selectedCollection)
            }
            .scrollIndicators(.hidden)
            .frame(height: 100)
        }
    }
}
