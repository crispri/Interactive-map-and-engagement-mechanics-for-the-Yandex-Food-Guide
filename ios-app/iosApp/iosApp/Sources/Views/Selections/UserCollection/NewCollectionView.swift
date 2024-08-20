//
//  NewCollectionView.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 20.08.2024.
//

import SwiftUI

struct NewCollectionView: View {
    @EnvironmentObject var viewModel: SnippetViewModel
    @State private var collectionName: String = ""
    @State private var collectionDescription: String = ""
    var mainAction: (() -> Void)?
    
    var body: some View {
        VStack(alignment: .leading) {
                Text("Новая коллекция")
                .font(.title2)
                .fontWeight(.bold)
                .padding(.bottom, 16)
            
            TextField("Название коллекции", text: $collectionName)
                .disableAutocorrection(true)
                .cornerRadius(8)
                .padding(.bottom, 7)
            Divider()
            TextField("Описание", text: $collectionDescription)
                .disableAutocorrection(true)
                .cornerRadius(8)
                .padding(.bottom, 7)
            Divider()
            Spacer()
            Button(action: {
                viewModel.userCollections += [UserCollection(
                    selection: SelectionDTO(
                        name: collectionName,
                        description: collectionDescription,
                        picture: "PlaceHolder"
                    ),
                    restaurantIDs: []
                )]
                mainAction?()
            }) {
                Text("Создать")
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(collectionName.isEmpty ? Color.gray.opacity(0.1) : Color(hex: 0xFCE000))
                    .foregroundColor(.black)
                    .cornerRadius(10)
            }
            .disabled(collectionName.isEmpty) // Disable the button if the name is empty
            
            Spacer()
        }
        .padding()
    }
}



#Preview {
    NewCollectionView()
        .environmentObject(SnippetViewModel())
}
