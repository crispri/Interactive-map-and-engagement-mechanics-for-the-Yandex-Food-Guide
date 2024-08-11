//
//  DetailsView.swift
//  iosApp
//
//  Created by Максим Кузнецов on 03.08.2024.
//

import SwiftUI
import BottomSheet

struct DetailsView: View {
    @Environment(\.dismiss) var dismiss
    @EnvironmentObject private var viewModel: SnippetViewModel
    @State private var isBottomSheetPresented = true
    @State var sheetPosition: BottomSheetPosition = .relative(SheetDetents.bottom.rawValue)
    @State private var sheetDetents = SheetDetents.bottom
    
    var body: some View {
        ZStack {
            YandexMapView()
                .edgesIgnoringSafeArea(.all)
                .environmentObject(viewModel.mapManager)
                .bottomSheet(
                    bottomSheetPosition: $sheetPosition,
                    switchablePositions: [
                        .relative(0.25),
                        .relative(0.5),
                        .relative(0.9)
                    ],
                    content: {
                        BottomSheetView(sheetDetents: $sheetDetents)
                    }
                )
                .enableAppleScrollBehavior(true)
                .showDragIndicator(false)
                .customBackground(
                    Color.clear
                        .clipShape(RoundedRectangle(cornerRadius: 20))
                )
            VStack {
                HStack {
                    backButton
                    Spacer()
                    userLocationButton
                    Spacer()
                    bookmarkButton
                }
                .padding()
                Spacer()
            }
        }
        .toolbar(.hidden, for: .navigationBar)
        .onAppear {
            viewModel.eventOnAppear()
        }
    }
    
    private var backButton: some View {
        Button {
            dismiss()
        } label: {
            Image(systemName: "arrow.left")
                .bold()
                .foregroundStyle(.black)
                .padding()
                .background(.white)
                .clipShape(Circle())
        }
        .shadow(radius: 20, x: 0, y: 8)
    }
    
    private var userLocationButton: some View {
        Button {
            viewModel.eventCenterCamera(to: .user)
        } label: {
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
    }
    
    private var bookmarkButton: some View {
        Button {
            // TODO: add action.
        } label: {
            Image(systemName: "bookmark")
                .bold()
                .foregroundStyle(.black)
                .padding()
                .background(.white)
                .clipShape(Circle())
        }
        .shadow(radius: 20, x: 0, y: 8)
    }
}

enum SheetDetents: CGFloat {
    case bottom = 0.25
    case medium = 0.5
    case large = 0.9
    
    func getPadding() -> CGFloat {
        let height = UIScreen.main.bounds.height
        return height * (1 - self.rawValue)
    }
}

#Preview {
    DetailsView()
        .environmentObject(SnippetViewModel())
}
