//
//  AddUserCollectionButton.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 20.08.2024.
//

import SwiftUI

struct AddUserCollectionButton: View {
    var mainAction: (() -> Void)?
    var body: some View {
        Button {
            mainAction?()
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
    AddUserCollectionButton()
}
