//
//  UserCollection.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 18.08.2024.
//

import SwiftUI

struct EditUserCollectionsView: View {
    @EnvironmentObject private var viewModel: SnippetViewModel
    @State var currentRestaurant: SnippetDTO
    
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
                    EditUserCollectionItem(userCollection: collection, restaurant: currentRestaurant)
                }
            }
        }
    }
}

struct AddUserCollectionButton: View {
    var body: some View {
        Button {
            // TODO: add action.
        } label: {
            Image(systemName: "plus")
                .bold()
                .foregroundStyle(.black)
                .padding()
        }
        .shadow(radius: 20, x: 0, y: 8)
    }
}

struct EditUserCollectionItem: View {
    @Binding var userCollection: UserCollection
    var restaurantsCount: Int {
        userCollection.restaurants.count
    }
    @State var restaurant: SnippetDTO
    
    @ViewBuilder
    func restaurantPicture() -> some View {
        if userCollection.selection.picture.contains("https?:")  { // #URL(...)
            let url = URL(string: userCollection.selection.picture)
            AsyncImage(url: url) { image in
                image
                    .resizable()
                    .frame(width: 56, height: 56)
                    .padding(.leading)
            } placeholder: { Color(.clear) }
        }
        else {
            Image(userCollection.selection.picture)
                .resizable()
                .frame(width: 56, height: 56)
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
            Button {
                // TODO:
            } label: {
                Image(systemName: restaurant.inCollection ? "checkmark" : "")
                    .frame(width: 32, height: 32)
                    .foregroundStyle(.black)
                    .background(Color(hex: 0x5C5A57).opacity(0.1))
                    .clipShape(RoundedRectangle(cornerSize: CGSize(width: 8, height: 8)))
            }
            .padding()

        }
    }
}

#Preview {
    EditUserCollectionsView(currentRestaurant: SnippetDTO(inCollection: true))
        .environmentObject(SnippetViewModel())
}
