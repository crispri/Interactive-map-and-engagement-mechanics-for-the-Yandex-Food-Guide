//
//  ContentView.swift
//  iosApp
//
//  Created by Максим Кузнецов on 03.08.2024.
//

import SwiftUI

struct GuideView: View {
    @StateObject var viewModel = SnippetViewModel()
    @State private var isUserCollectionsViewPresented: Bool = false

    var body: some View {
        NavigationStack {
            VStack {
                HStack {
                    Image("arrow-left")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 24, height: 24)
                    Spacer()
                    
                    userLocation
                    
                    Spacer()
                    
                    Image("bookmark-off")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 24, height: 24)
                        .onTapGesture {
                            isUserCollectionsViewPresented.toggle()
                        }
                }
                .padding(.horizontal)
                
                Image("search")
                    .resizable()
                    .scaledToFit()
                    .padding(.horizontal)
                
                smallMap
                    .padding(.top, 8)
                
                Image("scroll")
                    .resizable()
                    .scaledToFit()
                
                Image("whereToGo")
                    .resizable()
                    .scaledToFit()
            }
            .toolbar(.hidden, for: .navigationBar)
            .sheet(isPresented: $isUserCollectionsViewPresented, content: {
                UserCollectionsView()
                    .environmentObject(viewModel)
            })
        }
    }
    
    private var userLocation: some View {
        VStack(spacing: 0) {
            HStack(spacing: 4) {
                Text("Ваше местоположение")
                    .font(.system(size: 13))
                    .foregroundStyle(.black)
                Image(systemName: "chevron.right")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 10, height: 10)
                    .foregroundStyle(.black)
            }
            Text(viewModel.userLocaitonTitle)
                .font(.system(size: 16, weight: .medium))
                .foregroundStyle(.black)
        }
    }
    
    private var smallMap: some View {
        ZStack {
            YandexMapView()
                .environmentObject(viewModel.mapManager)
                .frame(height: 90)
                .cornerRadius(20)
                .padding(.horizontal)
                .onAppear {
                    viewModel.eventOnAppearForMain()
                }
            
            NavigationLink {
                DetailsView()
                    .environmentObject(viewModel)
            } label: {
                HStack {
                    Text("Выбрать на карте")
                    Image(systemName: "chevron.right")
                }
                .padding(.vertical, 8)
                .padding(.horizontal)
                .foregroundStyle(.black)
                .background(.white)
                .cornerRadius(20)
            }

        }
    }
}

#Preview {
    GuideView()
}
