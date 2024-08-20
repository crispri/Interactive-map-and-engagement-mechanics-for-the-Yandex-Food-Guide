//
//  ContentView.swift
//  iosApp
//
//  Created by Максим Кузнецов on 03.08.2024.
//

import SwiftUI

struct GuideView: View {
    @StateObject var viewModel = SnippetViewModel()
    @State private var seach = ""

    var body: some View {
        NavigationStack {
            VStack {
                Image("seach")
                ZStack {
                    YandexMapView()
                        .environmentObject(viewModel.mapManager)
                        .frame(width: 343, height: 90)
                        .cornerRadius(20)
                        .onAppear{
                            viewModel.eventOnAppear()
                    }

                    NavigationLink {
                        DetailsView()
                            .environmentObject(viewModel)
                    } label: {
                        HStack {
                            Text("Выбрать на карте")
                            Image(systemName: "chevron.right")
                        }
                        .padding(6)
                        .foregroundStyle(.black)
                        .background(.white)
                        .cornerRadius(20)
                    }

                }
                Image("scroll")
                Image("whereToGo")
            }
            .navigationTitle("Гид")
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    NavigationLink {
                        DetailsView()
                            .environmentObject(viewModel)
                    } label: {
                        Image(systemName: "bookmark")
                            .foregroundStyle(.black)
                    }
                }
            }
            .navigationBarTitleDisplayMode(.inline)
        }
    }
}

#Preview {
    GuideView()
}
