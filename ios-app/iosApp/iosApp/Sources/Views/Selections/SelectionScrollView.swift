//
//  SelectionStackView.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 03.08.2024.
//

import SwiftUI

struct SelectionScrollView: View {
    @EnvironmentObject var viemModel: SnippetViewModel
    
    var body: some View {
        ScrollView(.horizontal) {
            LazyHStack {
                PersonalSelectionView()
                ForEach(viemModel.collections) { collection in
                    SelectionView(title: collection.name, desc: collection.description)
                        .onTapGesture {
                            // TODO:
                            print(collection.name)
                        }
                }
            }
            .padding(.horizontal)
        }
        .scrollIndicators(.hidden)
    }
}

#Preview {
    SelectionScrollView()
        .environmentObject(SnippetViewModel())
}
