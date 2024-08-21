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
//    @State var type: SelectionType
    @Binding var selected: Bool
    var mainAction: (() -> Void)?
    var bookmarkAction: (() -> Void)?
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
                            Image(systemName: "bookmark")
                                .tint(.white)
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
                                Color.gray.opacity(0.5)
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
    
    enum SelectionType {
        case common
        case ultima
        case openKitchen
    }
}

#Preview {
    SelectionView(
        title: "Культура завтраков вне дома: какой-то фестиваль в Москве",
        desc: "12 мест",
        imageUrlString: "",
//        type: .ultima,
        selected: .constant(true)
    )
}
