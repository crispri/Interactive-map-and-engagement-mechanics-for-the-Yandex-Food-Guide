//
//  FilterDetailedView.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 16.08.2024.
//

import SwiftUI
import WrappingHStack

struct FilterDetailedView: View {
    @EnvironmentObject private var viewModel: SnippetViewModel
    
    var body: some View {
        ScrollView {
            VStack {
                ForEach($viewModel.filterCategories) { filterCategory in
                    SectionView(filterCategory: filterCategory)
                }
            }
        }
        .background( Color(.systemGroupedBackground).edgesIgnoringSafeArea(.all) )
    }
}

struct SectionView: View {
    @Binding var filterCategory: FilterCategory
    
    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            if !filterCategory.title.isEmpty {
                Text(filterCategory.title)
                    .font(.custom("YS-Bold", size: 20))
//                    .font(.title2)
                    .fontWeight(.bold)
                    .padding(.leading, 8)
            }
            
            WrappingHStack(alignment: .leading) {
                ForEach($filterCategory.filters) { filter in
                    TagView(filter: filter)
                }
            }
        }
        .padding()
        .background(.white)
        .clipShape(RoundedRectangle(cornerSize: CGSize(width: 20, height: 10)))
    }
    
}

struct TagView: View {
    @EnvironmentObject private var viewModel: SnippetViewModel
    @Binding var filter: Filter

    var body: some View {
        Button {
            filter.isActive.toggle()
            viewModel.mapManager.disablePins()
            viewModel.mapManager.cleanPins()
            viewModel.onCameraMove()
        } label: {
            Text(filter.name)
                .font(.custom("YS-Regular", size: 14))
//                .font(.system(size: 14, weight: .medium))
                .foregroundStyle(filter.isActive ? .white : .black)
                .tint(.primary)
                .padding(16)
                .background(filter.isActive ? .black : Color(hex: 0x5C5A57).opacity(0.1))
                .clipShape(Capsule())
                .lineLimit(1)
                .minimumScaleFactor(0.5)
        }
        
    }
}

#Preview {
    FilterDetailedView()
        .environmentObject(SnippetViewModel())
}
