//
//  SelectionStackView.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 03.08.2024.
//

import SwiftUI

struct SelectionScrollViewView: View {
    @EnvironmentObject var viemModel: SnippetViewModel
    
    var body: some View {
        ScrollView(.horizontal) {
            LazyHStack {
                PersonalSnippetView()
                ForEach(viemModel.collections) { collection in
                    SnippetView(title: collection.name, desc: collection.description)
                }
            }
            .padding(.leading, 5)
        }
    }
}

#Preview {
    SelectionScrollViewView()
        .environmentObject(SnippetViewModel())
}
