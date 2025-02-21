//
//  RestaurantView.swift
//  iosApp
//
//  Created by Христина Буянова on 09.08.2024.
//

import SwiftUI

struct RestaurantView: View {
    @Environment(\.dismiss) private var dismiss
    @State var restaurant: SnippetDTO
    @Binding var isEditUserCollectionsPresented: Bool
    var mainAction: (() -> Void)?
    
    var body: some View {
        ScrollView(.vertical) {
            VStack {
                ZStack {
                    if !restaurant.interior.isEmpty {
                        AsyncImage(url: URL(string: restaurant.interior[0])) { image in
                            image
                                .resizable()
                                .aspectRatio(3/2, contentMode: .fill)
                                .frame(width: 200)
                        } placeholder: {
                            ProgressView()
                        }
                    } else {
                        Image("noPhoto")
                            .resizable()
                            .aspectRatio(3/2, contentMode: .fill)
                            .frame(width: 200)
                    }

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
                        .padding(.top, 275)
                }
                Gallery()
                    .frame(height: 250)
                
                VStack {
                    ChatGPT()
                        .padding()
                        .cornerRadius(20)
                    ComentYGPT()
                        .padding()
                        .background(.white)
                        .clipShape(.rect(cornerRadius: 16))
                        .padding([.leading, .trailing, .bottom], 16)
                }
                .padding(.horizontal)
                .background(Color.chatGPT)
                .clipShape(.rect(cornerRadius: 16))
            }
        }
        .edgesIgnoringSafeArea(.all)
        .onAppear {
            let params: [AnyHashable : Any]? = [
                "user_id": "iOS user id",
                "timestamp": Int(Date().timeIntervalSince1970),
                "restaurant_id": restaurant.id
            ]
            MetricaManager.logEvent(
                name: "open_on_full_screen_restaurant_card",
                params: params
            )
        }
    }
    
    //    кнопки назад и поделиться
    private func ButtonBackAndShare() -> some View {
        
        HStack {
            Button {
                dismiss()
            } label: {
                Image(systemName: "arrow.left.circle.fill")
                    .resizable()
                    .frame(width: 36, height: 36)
                    .foregroundStyle(.white)
                    .opacity(0.95)
            }
            Spacer()
            Button {
                // TODO: action
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
        Text(restaurant.name)
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
            Text(String(format: "%.1f" , restaurant.rating))
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
            mainAction?()
            isEditUserCollectionsPresented.toggle()
            restaurant.inCollection.toggle()
        }, label: {
            Image("Bookmark")
                .renderingMode(.template)
                .bold()
                .foregroundStyle(restaurant.inCollection ? .white : .black)
                .frame(width: 45, height: 45)
                .background(restaurant.inCollection ? .black : .white)
                .opacity(0.9)
                .clipShape(.rect(cornerRadius: 16))
        })
    }
    
    private func BadgesTrain() -> some View {
        ScrollView(.horizontal) {
            HStack {
                ForEach(restaurant.tags, id:\.self) { tag in
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
                if restaurant.interior.count >= 2 {
                    AsyncImage(url: URL(string: restaurant.interior[1])) { image in
                        image
                            .resizable()
                            .cornerRadius(20)
                            .aspectRatio(1.3, contentMode: .fit)
                    } placeholder: {
                        ProgressView()
                    }
                } else if restaurant.interior.count == 1 {
                    AsyncImage(url: URL(string: restaurant.interior[0])) { image in
                        image
                            .resizable()
                            .cornerRadius(20)
                            .aspectRatio(1.3, contentMode: .fit)
                    } placeholder: {
                        ProgressView()
                    }
                } else {
                    Image("noPhoto")
                        .resizable()
                        .cornerRadius(20)
                        .aspectRatio(1.3, contentMode: .fit)
                }

                VStack {

                    if restaurant.interior.count >= 4 {
                        AsyncImage(url: URL(string: restaurant.interior[2])) { image in
                            image
                                .resizable()
                                .cornerRadius(20)
                        } placeholder: {
                            ProgressView()
                        }

                        AsyncImage(url: URL(string: restaurant.interior[3])) { image in
                            image
                                .resizable()
                                .cornerRadius(20)
                        } placeholder: {
                            ProgressView()
                        }
                    } else {
                        Image("noPhoto")
                            .resizable()
                            .cornerRadius(20)
                        Image("noPhoto")
                            .resizable()
                            .cornerRadius(20)
                    }
                }
                .aspectRatio(2/3, contentMode: .fit)
            }
            .padding(.horizontal)
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
                        .font(.system(size: 11))
                    Image(systemName: "chevron.right")
                }
                .foregroundStyle(.gray)
            }
        }
    }
    
    private func ComentYGPT() -> some View {
        Text(restaurant.description)
            .font(.system(size: 14))
    }
}

#Preview {
    RestaurantView(restaurant: SnippetDTO.mockData[2], isEditUserCollectionsPresented: Binding.constant(false))
}
