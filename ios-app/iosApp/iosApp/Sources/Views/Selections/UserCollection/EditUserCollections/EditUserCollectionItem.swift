//
//  EditUserCollectionItem.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 21.08.2024.
//

import SwiftUI

struct EditUserCollectionItem: View {
    @EnvironmentObject private var viewModel: SnippetViewModel
    @State var collection: UserCollection
    @State var restaurantsCount: Int = 0
    @State var inCollection: Bool = false
    var mainAction: (() -> Void)?
    var addToCollection: (() -> Void)?
    var removeFromCollection: (() -> Void)?
    
    var body: some View {
        HStack {
            RestaurantPicture(picture: collection.picture)
            VStack(alignment: .leading) {
                Text(collection.name)
                    .bold()
                    .font(.system(size: 16))
                Text(restaurantsCount == 0 ? "Пока ничего не сохранено" : "Сохранено мест:  \(restaurantsCount)")
                    .foregroundStyle(.gray)
                    .font(.system(size: 13))
            }
            Spacer()
            Button {
                if inCollection {
                    removeFromCollection?()
                    inCollection = false
                    restaurantsCount -= 1
                } else {
                    addToCollection?()
                    inCollection = true
                    restaurantsCount += 1
                }
            } label: {
                Image("Check")
                    .frame(width: 32, height: 32)
                    .opacity(inCollection ? 1 : 0)
                    .background(inCollection ? Color(hex: 0x302F2D) : Color(hex: 0x5C5A57).opacity(0.1))
                    .clipShape(RoundedRectangle(cornerSize: CGSize(width: 8, height: 8)))
            }
            .padding()
        }
        .task {
            await loadData(for: collection)
        }
    }
    
    private func loadData(for collection: UserCollection) async {
        let count = await collection.count
        let inCollection = await collection.contains(viewModel.currentRestaurantID ?? "")
        self.restaurantsCount = count
        self.inCollection = inCollection
    }
}
