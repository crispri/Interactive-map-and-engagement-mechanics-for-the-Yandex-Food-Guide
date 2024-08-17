//
//  BigPinSUI.swift
//  iosApp
//
//  Created by Христина Буянова on 14.08.2024.
//

import SwiftUI

struct BigPinSUI: View {

    @State var name: String
    @State var rating: Double

    var body: some View {
            Image("1rest")
                .resizable()
                .clipShape(RoundedCorner(radius: 16, corners: [.topLeft, .topRight]))
                .frame(width: 160, height: 60)
                .overlay {
                    VStack {
                        HStack {
                            Text(name)
                                .font(.system(size: 13))
                                .bold()
                                .lineLimit(1)
                                .multilineTextAlignment(.leading)
                            Image(systemName: "star.fill")
                                .resizable()
                                .frame(width: 11, height: 11)
                                .foregroundStyle(.white)
                            Text(String(format: "%.1f" , rating))
                                .font(.system(size: 11))
                                .multilineTextAlignment(.trailing)
                        }
                        HStack {
                            Text("кофе от 300р")
                                .padding(.leading, 16)
                                .font(.system(size: 11))
                                .lineLimit(1)
                            .multilineTextAlignment(.leading)
                            Spacer()

                        }

                    }
                    .foregroundStyle(.white)
                    .frame(width: 160, height: 43)
                    .background(.black)
                    .clipShape(.rect(cornerRadius: 16))
                    .padding(.top, 79)
                    .overlay {
                        Image("BottomCenter_black")
                            .resizable()
                            .frame(width: 10, height: 8)
                            .padding(.top, 131)
                    }
                }
    }

}

#Preview {
    BigPinSUI(name: "Хороший бар", rating: 4.11111)
}
