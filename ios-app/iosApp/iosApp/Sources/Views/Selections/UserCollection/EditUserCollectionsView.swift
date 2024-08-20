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
                ForEach(viewModel.userCollections.indices, id: \.self) { index in
                    EditUserCollectionItem(
                        picture: viewModel.userCollections[index].selection.picture ?? "PlaceHolder",
                        name: viewModel.userCollections[index].selection.name,
                        restaurantsCount: viewModel.userCollections[index].count,
                        id: viewModel.userCollections[index].id,
                        inCollection: Binding(get: {
                            viewModel.userCollections[index].contains(viewModel.currentRestaurantID!)
                        },
                                              set: {
                                                  inCollection in if inCollection { viewModel.userCollections[index].restaurantIDs.insert(
                                                    viewModel.currentRestaurantID!
                                                  ) } else { viewModel.userCollections[index].restaurantIDs.remove(
                                                    viewModel.currentRestaurantID!
                                                  )
                                                  }
                                              }),
                        addToCollection: {
                            viewModel.addRestaurant(to: viewModel.userCollections[index].id, restaurantID: viewModel.currentRestaurantID!)
                        },
                        removeFromCollection: {
                            viewModel.removeRestaurant(from: viewModel.userCollections[index].id, restaurantID: viewModel.currentRestaurantID!)
                        }
                    )
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

struct EditUserCollectionItem: View {
    @State var picture: String
    @State var name: String
    @State var restaurantsCount: Int
    @State var id: String
    @Binding var inCollection: Bool
    var addToCollection: (() -> Void)?
    var removeFromCollection: (() -> Void)?
    
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
    }
}

#Preview {
    EditUserCollectionsView()
}
