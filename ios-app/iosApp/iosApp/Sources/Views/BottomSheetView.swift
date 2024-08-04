//
//  BottomSheetView.swift
//  iosApp
//
//  Created by Максим Кузнецов on 04.08.2024.
//

import SwiftUI

struct BottomSheetView: View {
    var body: some View {
        VStack {
            FilterView()
                .padding(.top, 16)
            ScrollView {
                
            }
        }
    }
}

#Preview {
    DetailsView()
        .environmentObject(SnippetViewModel())
}
