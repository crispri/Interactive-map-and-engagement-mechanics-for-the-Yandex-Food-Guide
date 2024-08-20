//
//  FilterView.swift
//  iosApp
//
//  Created by Максим Кузнецов on 04.08.2024.
//

import SwiftUI

struct FilterView: View {
    @Binding var isFiltersPresented: Bool
    @Binding var filterCategories: [FilterCategory]
    @EnvironmentObject var vm: SnippetViewModel
    
    var body: some View {
        ScrollView(.horizontal) {
            HStack(spacing: 8) {
                firstFilterButton
                filterButton(filter: $filterCategories[0].filters[1])
                filterButton(filter: $filterCategories[1].filters[0])
                filterButton(filter: $filterCategories[0].filters[0])
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
}

struct filterButton: View {
    @Binding var filter: Filter
    @EnvironmentObject var vm: SnippetViewModel
    
    var body: some View {
        Button {
            filter.isActive.toggle()
            vm.mapManager.disablePins()
            vm.mapManager.cleanPins()
            vm.onCameraMove()
            print(filter.isActive)
        } label: {
            Text(filter.name)
                .font(.custom("YS-Regular", size: 13))
//                .font(.system(size: 13, weight: .medium))
                .tint(.primary)
                .foregroundStyle(filter.isActive ? .white : .black)
                .padding(10)
                .background(filter.isActive ? .black : Color(hex: 0x5C5A57).opacity(0.1))
                .clipShape(Capsule())
        }
    }
}

#Preview {
    FilterView(isFiltersPresented: DetailsView().$isFiltersPresented, filterCategories: Binding(get: {
        SnippetViewModel().filterCategories
    }, set: { _ in } ))
}
