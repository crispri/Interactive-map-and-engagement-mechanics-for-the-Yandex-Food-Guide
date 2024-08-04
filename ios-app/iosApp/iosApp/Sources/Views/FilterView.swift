//
//  FilterView.swift
//  iosApp
//
//  Created by Максим Кузнецов on 04.08.2024.
//

import SwiftUI

struct FilterView: View {
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
            // TODO: add action.
        } label: {
            Image(systemName: "slider.horizontal.3")
                .foregroundStyle(.black)
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
                .foregroundStyle(.black)
                .padding(10)
                .background(Color(hex: 0x5C5A57).opacity(0.1))
                .clipShape(Capsule())
        }
    }
}

#Preview {
    FilterView()
}
