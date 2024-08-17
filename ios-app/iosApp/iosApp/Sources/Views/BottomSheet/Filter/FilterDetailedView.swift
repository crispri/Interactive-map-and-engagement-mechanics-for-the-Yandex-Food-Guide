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
                SectionView(
                    title: "Время работы",
                    tags: [
                        Tag(text: "Открыто сейчас"),
                        Tag(text: "Круглосуточно"),
                        Tag(text: "Кальянная 2"),
                        Tag(text: "Бар2"),
                        Tag(text: "Кальянная 3"),
                        Tag(text: "Бар3"),
                        Tag(text: "Кальянная 4")
                    ]
                    )
                SectionView(
                    title: "Тип заведения",
                    tags: [
                        Tag(text: "Банкетный зал "),
                        Tag(text: "Бар"),
                        Tag(text: "Кальянная 2"),
                        Tag(text: "Бар2"),
                        Tag(text: "Кальянная 3"),
                        Tag(text: "Бар3"),
                        Tag(text: "Кальянная 4")
                    ]
                )
                
            }
            
        }
        .background( Color(.systemGroupedBackground).edgesIgnoringSafeArea(.all) )
    }
}

struct SectionView: View {
    @EnvironmentObject private var viewModel: SnippetViewModel
    let title: String
    var tags: [Tag]
    
    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            if !title.isEmpty {
                Text(title)
                    .font(.title2)
                    .fontWeight(.bold)
                    .padding(.leading, 8)
            }
            
            WrappingHStack(alignment: .leading) {
                ForEach(tags) {
                    TagView(
                        text: $0.text,
                        isActive: Binding.constant(false)
                    )
                }
            }
            
            Spacer(minLength: .zero)
        }
        .padding()
        .background(.white)
        .clipShape(RoundedRectangle(cornerSize: CGSize(width: 20, height: 10)))
    }
    
}

struct TagView: View {
    let text: String
    @EnvironmentObject var viewModel: SnippetViewModel
    @Binding var isActive: Bool

    var body: some View {
        Button {
            isActive.toggle()
//            viewModel.tags[text] = isActive
            print(isActive)
        } label: {
            Text(text)
                .font(.system(size: 14, weight: .medium))
                .foregroundStyle(isActive ? .white : .black)
                .tint(.primary)
                .padding(16)
                .background(isActive ? .black : Color(hex: 0x5C5A57).opacity(0.1))
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
