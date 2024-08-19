//
//  UserCollectionsView.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 18.08.2024.
//

import SwiftUI

struct UserCollectionsView: View {
    @EnvironmentObject private var viewModel: SnippetViewModel
    
    var body: some View {
        VStack {
            HStack {
                Text("**Коллекции**")
                    .font(.title2)
                    .padding(.leading)
                Spacer()
                AddUserCollectionButton()
            }
            ScrollView {
                ForEach($viewModel.userCollections) { collection in
                    UserCollectionItem(userCollection: collection)
                }
            }
        }
    }
}

struct UserCollectionItem: View {
    @Binding var userCollection: UserCollection
    var restaurantsCount: Int {
        userCollection.restaurantIDs.count
    }
    
    @ViewBuilder
    func restaurantPicture() -> some View {
        if userCollection.selection.picture.contains("http")  { // #URL(...)
            let url = URL(string: userCollection.selection.picture)
            AsyncImage(url: url) { image in
                image
                    .resizable()
                    .frame(width: 84, height: 84)
                    .clipShape(.rect(cornerRadius: 16))
                    .padding(.leading)
            } placeholder: { Color(.clear) }
        }
        else {
            Image(userCollection.selection.picture)
                .resizable()
                .frame(width: 84, height: 84)
                .padding(.leading)
        }
    }
    
    var body: some View {
        HStack {
            restaurantPicture()
            VStack(alignment: .leading) {
                Text(userCollection.selection.name)
                    .bold()
                    .font(.system(size: 16))
                Text(restaurantsCount == 0 ? "Пока ничего не сохранено" : "Сохранено мест:  \(restaurantsCount)")
                    .foregroundStyle(.gray)
                    .font(.system(size: 13))
            }
            Spacer()
        }
    }
}

#Preview {
    UserCollectionsView()
        .environmentObject(SnippetViewModel())
}
