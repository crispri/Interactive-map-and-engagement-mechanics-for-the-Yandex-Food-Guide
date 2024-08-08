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
                    Button {
                        // TODO: add action.
                    } label: {
                        SelectionView(title: collection.name, desc: collection.description)
                    }
                }
            }
            .padding(.horizontal)
        }
        .scrollIndicators(.hidden)
        .frame(height: 80)
    }
}

#Preview {
    SelectionScrollView()
        .environmentObject(SnippetViewModel())
}
