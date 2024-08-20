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
                ForEach(viewModel.userCollections) { collection in
                    UserCollectionItem(
                        picture: collection.selection.picture ?? "PlaceHolder",
                        name: collection.selection.name,
                        restaurantsCount: collection.count
                    )
                }
            }
        }
    }
}

struct UserCollectionItem: View {
    @State var picture: String
    @State var name: String
    @State var restaurantsCount: Int
    
    var body: some View {
        HStack {
            RestaurantPicture(picture: picture)
            VStack(alignment: .leading) {
                Text(name)
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
