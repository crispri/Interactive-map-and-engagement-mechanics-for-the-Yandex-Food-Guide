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
                            imageUrlString: viewModel.selections[index].picture ?? "PersonalSelection",
                            selected: Binding(
                                get: {
                                    if let selectedCollection = viewModel.currentSelection,
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
                            toggleSelectionInCollections(index: index)
                        } isSavedToCollection: {
                            viewModel.userCollections.contains { $0.id == viewModel.selections[index].id }
                        } infoAction: {
                            // TODO: add infoAction.
                        }
                        .id(index)
                    }
                }
                .padding(.horizontal)
                .animation(.spring(duration: 0.2), value:  viewModel.currentSelection)
            }
            .scrollIndicators(.hidden)
            .frame(height: 100)
        }
    }
    
    private func toggleSelectionInCollections(index: Int) {
            let selectionToAdd = UserCollection(selection: viewModel.selections[index])
            if viewModel.userCollections.contains(selectionToAdd) {
                viewModel.userCollections = viewModel.userCollections.filter { $0.id != selectionToAdd.id }
            } else {
                viewModel.userCollections.append(selectionToAdd)
            }
    }
}
