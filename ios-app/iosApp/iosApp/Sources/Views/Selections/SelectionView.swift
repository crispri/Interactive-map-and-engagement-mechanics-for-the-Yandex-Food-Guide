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
            VStack(spacing: 0) {
                if selected {
                    HStack {
                        Button {
                            bookmarkAction?()
                        } label: {
                            Image(systemName: "info.circle")
                                .tint(.white)
                        }
                        Spacer()
                        
                        Text("9 мест")
                            .font(.system(size: 11))
                            .foregroundStyle(.white)
                            .multilineTextAlignment(.center)
                        
                        Spacer()
                        Button {
                            bookmarkAction?()
                        } label: {
                            Image(isSavedToCollection() ? "Bookmark.fill" : "Bookmark")
                                .renderingMode(.template)
                                .resizable()
                                .frame(width: 12, height: 15)
                                .bold()
                                .tint(.white)
                                .padding(.trailing, 4)
                                .padding(.top, 0)
                        }
                    }
                    .frame(height: 16)
                    .padding(.horizontal, 8)
                    .padding(.vertical, 4)
                }
                
                
                Text(title)
                    .font(.system(size: 13))
                    .fontWeight(.semibold)
                    .foregroundStyle(.white)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal, 8)
            }
            .shadow(color: .black, radius: 10)
            .frame(width: selected ? 300 : 146, height: 60)
            .background{
                if let url = URL(string: imageUrlString) {
                    AsyncImage(url: url) { image in
                        image
                            .resizable()
                            .scaledToFill()
                            .overlay {
                                LinearGradient(
                                    gradient: Gradient(colors: [Color.black.opacity(0.1), Color.black.opacity(0.8)]),
                                    startPoint: .top,
                                    endPoint: .bottom
                                )
                            }
                    } placeholder: { Color.gray }
                } else {
                    Color.gray
                }
            }
            .clipShape(RoundedRectangle(cornerRadius: 16))
        }
        .animation(.easeInOut, value: selected)
        
    }
}

#Preview {
    SelectionScrollView()
        .environmentObject(SnippetViewModel())
}
