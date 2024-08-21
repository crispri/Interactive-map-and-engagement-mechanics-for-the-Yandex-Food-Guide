//
//  SelectionView.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 03.08.2024.
//

import SwiftUI

struct SelectionView: View {
    @State var title: String
    @State var desc: String
    @State var imageUrlString: String
    @Binding var selected: Bool
    var mainAction: (() -> Void)?
    var bookmarkAction: (() -> Void)?
    var isSavedToCollection: (() -> Bool)
    var infoAction: (() -> Void)?
    
    var body: some View {
        Button {
            mainAction?()
        } label: {
            HStack(alignment: .bottom) {
                if selected {
                    Button {
                        bookmarkAction?()
                    } label: {
                        Image(isSavedToCollection() ? "Bookmark.fill" : "Bookmark")
                            .renderingMode(.template)
                            .bold()
                            .tint(.white)
                            .padding([.bottom, .leading], 8)
                    }
                    .frame(width: 40, height: 40, alignment: .bottomLeading)
                }
                Spacer()
                
                VStack {
                    Text(title)
                        .font(.system(size: 13))
                        .fontWeight(.semibold)
                        .foregroundStyle(.white)
                        .shadow(color: .black, radius: 4)
                        .multilineTextAlignment(.center)
                        .lineLimit(selected ? 1 : 2)
//                        .shadow(color: .black, radius: 4, x: 5, y: 5)
                    if selected {
                        Text(desc)
                            .foregroundStyle(.white)
                            .multilineTextAlignment(.center)
                            .lineLimit(2)
                            .font(.system(size: 13))
//                            .shadow(color: .black, radius: 4, x: 5, y: 5)
                    }
                }
                .padding(.vertical, 8)
                .padding(.horizontal, selected ? 0 : 8)
                
                Spacer()
                if selected {
                    Button {
                        infoAction?()
                    } label: {
                        Image(systemName: "info.circle")
                            .tint(.white)
                            .padding([.bottom, .trailing], 8)
                    }
                    .frame(width: 40, height: 40, alignment: .bottomTrailing)
                }
            }
            .background(
                ZStack {
                    if let url = URL(string: imageUrlString) {
                        AsyncImage(url: url) { image in
                            image
                                .resizable()
                                .scaledToFill()
                                .frame(
                                    width: selected ? UIScreen.main.bounds.width - 100 : 145.73,
                                    height: selected ? 80 : 60.05
                                )
                        } placeholder: { Color.gray.opacity(0.5) }
                    }
                    LinearGradient(gradient: Gradient(colors: [Color.black.opacity(0.1), Color.black.opacity(0.8)]), startPoint: .top, endPoint: .bottom)
                }
                
            )
            .frame(
                width: selected ? UIScreen.main.bounds.width - 100 : 145.73,
                height: selected ? 80 : 60.05
            )
            .clipShape(RoundedRectangle(cornerRadius: 16.19))
            .animation(.spring(duration: 0.2), value: selected)
        }
    }
}

#Preview {
    SelectionScrollView()
        .environmentObject(SnippetViewModel())
}
