//
//  UserCollection.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 18.08.2024.
//

import SwiftUI

struct EditUserCollectionsView: View {
    @EnvironmentObject private var viewModel: SnippetViewModel
    @Binding var restaurant: SnippetDTO
    
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
                    EditUserCollectionItem(userCollection: collection, 
                                           restaurant: $restaurant)
                }
            }
        }
    }
}

struct EditUserCollectionItem: View {
    @Binding var userCollection: UserCollection
    @Binding var restaurant: SnippetDTO
    
    @ViewBuilder
    func restaurantPicture() -> some View {
        if userCollection.selection.picture.contains("http")  { // #URL(...)
            let url = URL(string: userCollection.selection.picture)
            AsyncImage(url: url) { image in
                image
                    .resizable()
                    .frame(width: 56, height: 56)
                    .clipShape(.rect(cornerRadius: 16))
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
                Text(userCollection.count == 0 ? "Пока ничего не сохранено" : "Сохранено мест:  \(userCollection.count)")
                    .foregroundStyle(.gray)
                    .font(.system(size: 13))
            }
            Spacer()
            Button {
                if userCollection.contains(restaurant.id) {
                    userCollection.restaurantIDs = userCollection.restaurantIDs.filter { $0 != restaurant.id }
                } else {
                    userCollection.restaurantIDs.insert(restaurant.id)
                }
                restaurant.inCollection.toggle()
            } label: {
                Image("Check")
                    .frame(width: 32, height: 32)
                    .opacity(userCollection.contains(restaurant.id) ? 1 : 0)
                    .background(userCollection.contains(restaurant.id) ? Color(hex: 0x302F2D) : Color(hex: 0x5C5A57).opacity(0.1))
                    .clipShape(RoundedRectangle(cornerSize: CGSize(width: 8, height: 8)))
            }
            .padding()
            
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

#Preview {
    EditUserCollectionsView(restaurant: Binding(get: {
        SnippetDTO()
    }, set: { _ in
        
    }))
        .environmentObject(SnippetViewModel())
}
