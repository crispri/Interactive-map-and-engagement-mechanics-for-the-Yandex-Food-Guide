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
                VStack {
                    Text(title)
                        .font(.system(size: 13))
                        .fontWeight(.semibold)
                        .foregroundStyle(.white)
                        .multilineTextAlignment(.center)
                        .lineLimit(selected ? 1 : 2)
                    if selected {
                        Text(desc)
                            .foregroundStyle(.white)
                            .multilineTextAlignment(.center)
                            .font(.system(size: 13))
                    }
                }
                .padding(.vertical, 8)
                .padding(.horizontal, selected ? 0 : 8)
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
                Image("Selection")
                    .resizable()
                    .scaledToFill()
                    .frame(
                        width: selected ? UIScreen.main.bounds.width - 100 : 145.73,
                        height: selected ? 80 : 60.05
                    )
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
