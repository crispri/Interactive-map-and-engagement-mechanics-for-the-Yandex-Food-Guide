//
//  BottomSheetView.swift
//  iosApp
//
//  Created by Максим Кузнецов on 04.08.2024.
//

import SwiftUI
import BottomSheet

struct BottomSheetView: View {
    @EnvironmentObject private var viewModel: SnippetViewModel
    
    var body: some View {
        ScrollView {
            VStack(spacing: 0) {
                ForEach(viewModel.snippets, id: \.self) { snippet in
                    SnippetCell(restaurant: snippet)
                        .sheet(isPresented: /*@START_MENU_TOKEN@*//*@PLACEHOLDER=Is Presented@*/.constant(false)/*@END_MENU_TOKEN@*/, content: {
                            /*@START_MENU_TOKEN@*//*@PLACEHOLDER=Content@*/Text("Sheet Content")/*@END_MENU_TOKEN@*/
                        })
                }
            }
            .padding(.bottom)
            .animation(.easeInOut, value: viewModel.snippets)
            .scrollTargetLayout()
            .background(
                Color.white
            )
        }
        .scrollTargetBehavior(.viewAligned)
    }
}
