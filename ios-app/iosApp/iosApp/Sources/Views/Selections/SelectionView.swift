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
                        Image(systemName: "bookmark")
                            .tint(.white)
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
                    if selected {
                        Text(desc)
                            .foregroundStyle(.white)
                            .multilineTextAlignment(.center)
                            .font(.system(size: 13))
                            .shadow(color: .black, radius: 4)
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
                    }
                    .frame(width: 40, height: 40, alignment: .bottomTrailing)
                }
            }
            .background {
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
            }
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
