//
//  ContentView.swift
//  iosApp
//
//  Created by Максим Кузнецов on 03.08.2024.
//

import SwiftUI

struct GuideView: View {
    @StateObject var viewModel = SnippetViewModel()
    
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
                            .environmentObject(viewModel)
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
