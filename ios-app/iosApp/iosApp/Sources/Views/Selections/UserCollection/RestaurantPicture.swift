//
//  RestaurantPicture.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 19.08.2024.
//

import SwiftUI

struct RestaurantPicture: View {
    @State var picture: String
    
    var body: some View {
        if picture.contains("http")  { // #URL(...)
            let url = URL(string: picture)
            AsyncImage(url: url) { image in
                image
                    .resizable()
                    .frame(width: 56, height: 56)
                    .clipShape(.rect(cornerRadius: 16))
                    .padding(.leading)
            } placeholder: { Color(.clear) }
        }
        else {
            Image(picture)
                .resizable()
                .frame(width: 56, height: 56)
                .padding(.leading)
        }
    }
}
