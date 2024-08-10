//
//  RestaurantView.swift
//  iosApp
//
//  Created by Христина Буянова on 09.08.2024.
//

import SwiftUI

struct RestaurantView: View {
    var tags = ["Итальянская", "Европейская"]

    var body: some View {
        ScrollView(.vertical) {
            VStack {
                ZStack {
                    Image("1rest")
                        .resizable()
                        .aspectRatio(3/2, contentMode: .fit)

                    VStack(alignment: .leading) {
                        ButtonBackAndShare()
                        Divider()
                        NameRest()
                        HStack {
                            Estimation()
                            VStack (alignment: .leading) {
                                Globe()
                                Favourites()
                            }
                        }
                    }
                    .padding()
                    .padding(.bottom, 10)

                    BadgesTrain()
                        .padding()
                        .frame(height: 50)
                        .background(.white)
                        .cornerRadius(24)
                        .padding(.top, 275)
                }
                Gallery()
                    .frame(height: 250)
                    .padding(.horizontal)

                VStack {
                    ChatGPT()
                    //                        .frame(width: 400, height: 60)
                        .border(Color.black)
                        .padding(16)
                        .cornerRadius(20)
                    ComentYGPT()
                        .padding(16)
                        .background(.white)
                        .clipShape(.rect(cornerRadius: 16))
//                        .frame(height: 100)
                }
                .background(Color.chatGPT)
                .clipShape(.rect(cornerRadius: 16))
                }
        }
        .edgesIgnoringSafeArea(.top)
    }

//    кнопки назад и поделиться
    private func ButtonBackAndShare() -> some View {

        HStack {
            Button {
                // TODO: add action.
            } label: {
                Image(systemName: "arrow.left.circle.fill")
                    .resizable()
                    .frame(width: 36, height: 36)
                    .foregroundStyle(.white)
                    .opacity(0.95)
            }
            Spacer()
            Button {
                // TODO: add action.
            } label: {
                Image(systemName: "square.and.arrow.up.circle.fill")
                    .resizable()
                    .frame(width: 36, height: 36)
                    .foregroundStyle(.white)
                    .opacity(0.95)
            }
        }
    }

//    имя ресторана
    private func NameRest() -> some View {
        Text("Мэлт")
            .foregroundStyle(.white)
            .bold()
            .font(.system(size: 30))
    }

//    рейтинг и оценки
    private func Estimation() -> some View {
        VStack {
            Image(systemName: "star.fill")
                .resizable()
                .frame(width: 24, height: 24)
                .padding(.top, 4)
            Text("5.0")
                .font(.system(size: 22))
                .bold()
            Spacer()
            Text("82")
                .foregroundStyle(.gray)
                .font(.system(size: 14))
            Text("оценки")
                .foregroundStyle(.gray)
                .font(.system(size: 14))
            Spacer()
        }
        .frame(width: 70, height: 110)
        .background(.white)
        .opacity(0.9)
        .cornerRadius(20)
    }

//    глобус
    private func Globe() -> some View {

        Button(action: {
            // TODO: add action.
        }, label: {
            Image(systemName: "globe")
                .frame(width: 45, height: 45)
                .foregroundStyle(.black)
                .background(.white)
                .opacity(0.9)
                .cornerRadius(15)
        })
    }

    //    избранное
    private func Favourites() -> some View {
        Button(action: {
            // TODO: add action.
        }, label: {
            Image(systemName: "bookmark")
                .frame(width: 45, height: 45)
                .foregroundStyle(.black)
                .background(.white)
                .opacity(0.9)
//                .cornerRadius(15)
                .clipShape(.rect(cornerRadius: 16))
        })
    }

    private func BadgesTrain() -> some View {
        ScrollView(.horizontal) {
            HStack {
                ForEach(tags, id:\.self) { tag in
                    Chips(text: tag)
                }
            }
        }
    }

    private func Chips(text: String) -> some View {
        Text(text)
            .padding([.trailing, .leading, .bottom, .top], 10)
            .font(.system(size: 11))
            .foregroundStyle(Color.grayNameChips)
            .background(Color.lightGrayChips)
            .cornerRadius(20)
    }

    private func Gallery() -> some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack {
                Image("1rest")
                    .resizable()
                    .cornerRadius(20)
                    .aspectRatio(1.3, contentMode: .fit)
                VStack {
                    Image("2rest")
                        .resizable()
                        .cornerRadius(20)
                    Image("3rest")
                        .resizable()
                        .cornerRadius(20)
                }
                .aspectRatio(2/3, contentMode: .fit)
            }
        }
    }

    private func ChatGPT() -> some View {
        HStack {
            VStack {
                Image(systemName: "circle.fill")
                    .foregroundStyle(.black)
                Image(systemName: "circle.fill")
                    .resizable()
                    .foregroundStyle(.black)
                    .frame(width: 7, height: 7)
                    .padding(.leading, 15)
            }
            VStack(alignment: .leading) {
                Text("Yandex GPT о месте")
                    .bold()
                HStack {
                    Text("сгенерировано нейросетью на основе отзывов")
                        .lineLimit(1)
                        .font(.system(size: 14))
                    Image(systemName: "chevron.right")
                }
                .foregroundStyle(.gray)
            }
        }
    }

    private func ComentYGPT() -> some View {
        Text("Коктейль-бар Мэлт - это стильное и уютное метсто, где вы можете насладиться вкусной едой и напитками. Здесь вы можете попробовать лучши коктейли в городе")
    }
}




#Preview {
    RestaurantView()
}
