//
//  SelectionStackView.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 03.08.2024.
//

import SwiftUI

struct SelectionScrollViewView: View {
    var body: some View {
        ScrollView(.horizontal) {
            LazyHStack {
                PersonalSelectionView()
                SelectionView(title: "Завтраки вне дома", desc: "Куда сходить · Места")
                SelectionView(title: "Завтраки вне дома", desc: "Куда сходить · Места")
                SelectionView(title: "Завтраки вне дома", desc: "Куда сходить · Места")
                SelectionView(title: "Завтраки вне дома", desc: "Куда сходить · Места")
                SelectionView(title: "Завтраки вне дома", desc: "Куда сходить · Места")
            }
            .padding(.leading, 5)
        }
    }
}

#Preview {
    SelectionScrollViewView()
}
