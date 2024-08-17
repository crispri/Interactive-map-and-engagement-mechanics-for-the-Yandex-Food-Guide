//
//  FilterView.swift
//  iosApp
//
//  Created by Максим Кузнецов on 04.08.2024.
//

import SwiftUI

struct FilterView: View {
    @Binding var isFiltersPresented: Bool

    var body: some View {
        ScrollView(.horizontal) {
            HStack(spacing: 8) {
                firstFilterButton
                filterButton(title: "Рядом со мной")
                filterButton(title: "Открыто сейчас")
                filterButton(title: "Высокий рейтинг")
            }
            .padding(.horizontal)
        }
        .scrollIndicators(.hidden)
    }
    
    private var firstFilterButton: some View {
        Button {
            isFiltersPresented.toggle()
        } label: {
            Image(systemName: "slider.horizontal.3")
                .tint(.primary)
                .padding(10)
                .background(Color(hex: 0x5C5A57).opacity(0.1))
                .clipShape(Circle())
        }
    }
    
    @ViewBuilder
    private func filterButton(title: String) -> some View {
        Button {
            // TODO: add action.
        } label: {
            Text(title)
                .font(.system(size: 13, weight: .medium))
                .tint(.primary)
                .padding(10)
                .background(Color(hex: 0x5C5A57).opacity(0.1))
                .clipShape(Capsule())
                .onTapGesture {

                }
        }
    }
}

#Preview {
    FilterView(isFiltersPresented: DetailsView().$isFiltersPresented)
}
