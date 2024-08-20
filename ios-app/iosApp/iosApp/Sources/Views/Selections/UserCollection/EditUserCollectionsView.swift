//
//  UserCollection.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 18.08.2024.
//

import SwiftUI

struct EditUserCollectionsView: View {
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
                ForEach($viewModel.userCollections) { $collection in
                    EditUserCollectionItem(
                        userCollection: $collection
                    )
                }
            }
        }
    }
}

struct EditUserCollectionItem: View {
    @EnvironmentObject var viewModel: SnippetViewModel
    @Binding var userCollection: UserCollection
    
    var body: some View {
        HStack {
            RestaurantPicture(userCollection: userCollection)
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
                if let RID = viewModel.currentRestaurantID {
                    if userCollection.contains(RID) {
                        userCollection.restaurantIDs = userCollection.restaurantIDs.filter { $0 != RID }
                    } else {
                        userCollection.restaurantIDs.insert(RID)
                    }
                }
                else { print("boockmark pressed but no RID") }
            } label: {
                Image("Check")
                    .frame(width: 32, height: 32)
                    .opacity(userCollection.contains(viewModel.currentRestaurantID ?? "") ? 1 : 0)
                    .background(userCollection.contains(viewModel.currentRestaurantID ?? "") ? Color(hex: 0x302F2D) : Color(hex: 0x5C5A57).opacity(0.1))
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
    EditUserCollectionsView()
}
