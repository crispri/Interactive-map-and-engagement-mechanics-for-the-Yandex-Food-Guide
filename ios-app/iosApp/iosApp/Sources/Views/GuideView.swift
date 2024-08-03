//
//  ContentView.swift
//  iosApp
//
//  Created by Максим Кузнецов on 03.08.2024.
//

import SwiftUI

struct GuideView: View {
    var body: some View {
        NavigationStack {
            VStack {
                Spacer()
            }
            .navigationTitle("Гид")
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    NavigationLink {
                        DetailsView()
                    } label: {
                        Image(systemName: "map")
                            .foregroundStyle(.black)
                    }
                }
            }
            .navigationBarTitleDisplayMode(.inline)
            .background{
                Image("Main")
                    .ignoresSafeArea(edges: .all)
            }
        }
    }
}

#Preview {
    GuideView()
}
