//
//  UserCollectionItem.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 21.08.2024.
//

import SwiftUI

struct UserCollectionItem: View {
    @EnvironmentObject private var viewModel: SnippetViewModel
    @State var collection: UserCollection
    @State var restaurantsCount: Int = 0
    @State var inCollection: Bool = false
    
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
