//
//  UserCollectionsView.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 18.08.2024.
//

import SwiftUI

struct UserCollectionsView: View {
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
                    UserCollectionItem(collection: collection)
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

#Preview {
    UserCollectionsView()
        .environmentObject(SnippetViewModel())
}
