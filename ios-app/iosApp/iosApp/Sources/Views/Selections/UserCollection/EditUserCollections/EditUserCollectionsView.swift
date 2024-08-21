//
//  UserCollection.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 18.08.2024.
//

import SwiftUI

struct EditUserCollectionsView: View {
    @EnvironmentObject private var viewModel: SnippetViewModel
    @State private var isNewCollectionViewPresented: Bool = false
    
    var body: some View {
        VStack {
            HStack {
                Text("**Коллекции**")
                    .font(.title2)
                    .padding(.leading)
                Spacer()
                AddUserCollectionButton() {
                    isNewCollectionViewPresented.toggle()
                }
            }
            ScrollView {
                ForEach(viewModel.userCollections) { collection in
                    EditUserCollectionItem(collection: collection) {
                        // TODO: mainAction
                    } addToCollection: {
                        Task {
                            do {
                                try await viewModel.putRestaurantTo(collectionID: collection.id, restaurantID: viewModel.currentRestaurantID ?? "")
                            } catch { print(error) }
                        }
                    } removeFromCollection: {
                        // TODO: removeFromCollection
                    }

                }
            }
        }
        .sheet(isPresented: $isNewCollectionViewPresented, content: {
            NewCollectionView() {
                isNewCollectionViewPresented.toggle()
            }
            .presentationDetents([.medium])
        })
    }
}
