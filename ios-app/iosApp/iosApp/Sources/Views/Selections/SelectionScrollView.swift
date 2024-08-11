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
                    ForEach(viewModel.collections.indices, id: \.self) { index in
                        SelectionView(
                            title: viewModel.collections[index].name,
                            desc: viewModel.collections[index].description,
                            selected: Binding(
                                get: {
                                    if let selectedCollection = viewModel.selectedCollection,
                                       selectedCollection == viewModel.collections[index] {
                                        return true
                                    }
                                    return false
                                },
                                set: { _ in }
                            )
                        ) {
                            if let selectedCollection = viewModel.selectedCollection,
                               selectedCollection == viewModel.collections[index] {
                                viewModel.selectedCollection = nil
                                viewModel.fetchSnippets()
                            } else {
                                viewModel.selectedCollection = viewModel.collections[index]
                                reader.scrollTo(index, anchor: .center)
                                viewModel.fetchSelectionSnippets(id: viewModel.selectedCollection?.id ?? "")
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

#Preview {
    SelectionScrollView()
        .environmentObject(SnippetViewModel())
}
