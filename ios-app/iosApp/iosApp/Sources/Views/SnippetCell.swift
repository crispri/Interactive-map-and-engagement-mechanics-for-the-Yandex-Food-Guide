//
//  InfoRestaurantCell.swift
//  iosApp
//
//  Created by Христина Буянова on 03.08.2024.
//

import SwiftUI

var textNames = ["1000-2500р", "Европейская", "Коктейли", "Завтрак"]

struct SnippetCell: View {
    @EnvironmentObject var viewModel: SnippetViewModel
    @State var restaurant: SnippetDTO

    var body: some View {
        VStack {
            ImageRest()
            NameRest()
            Meta()
            HStack {
                Description()
                    .multilineTextAlignment(.leading)
               Spacer()
            }
            BadgesTrain()
        }
        .padding()
    }

    private func ImageRest() -> some View {
        ZStack(alignment: .topTrailing) {
            Image("1rest")
                .resizable()
                .aspectRatio(contentMode: .fill)
                .frame(height: 200)
                .cornerRadius(24)

            ZStack {
                Image(systemName: "circle.fill")
                    .resizable()
                    .frame(width: 32, height: 32)
                    .foregroundStyle(.black)
                    .opacity(0.1)
                Image(systemName: "bookmark")
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 24, height: 24)
                    .bold()
                .foregroundStyle(.white)
            }
            .padding([.top, .trailing], 10.0)
        }
    }

    private func NameRest() -> some View {
        HStack {
            Text(restaurant.name)
                .bold()
                .font(.system(size: 16))
            Spacer()
            HStack {
                Image(systemName: "star.fill")
                    .resizable()
                    .frame(width: 12, height: 12)
                Text("\(restaurant.rating)")
                    .font(.system(size: 14))
            }
        }
    }

    private func Meta() -> some View {
        HStack {
            Text("До 00:00")
            Image(systemName: "circle.fill")
                .resizable()
                .frame(width: 2, height: 2)
            Text("м. Китай-город")
            Image(systemName: "circle.fill")
                .resizable()
                .frame(width: 2, height: 2)
            Text("28 мин на машине")
            Spacer()
        }
        .font(.system(size: 13))
    }

    private func Description() -> some View {
        Text(restaurant.description)
            .foregroundStyle(.gray)
            .lineLimit(2)
            .font(.system(size: 13))
    }

    private func BadgesTrain() -> some View {
        ScrollView(.horizontal) {
            HStack {
                ForEach(textNames, id:\.self) { textName in
                    Chips(text: textName)
                }
            }
        }
    }

    private func Chips(text: String) -> some View {
        Text(text)
            .padding([.trailing, .leading, .bottom, .top], 4)
            .font(.system(size: 11))
            .foregroundStyle(Color.grayNameChips)
            .background(Color.lightGrayChips)
            .cornerRadius(20)
    }
}

#Preview {
    SnippetCell(restaurant: SnippetViewModel().snippets[0])
}
