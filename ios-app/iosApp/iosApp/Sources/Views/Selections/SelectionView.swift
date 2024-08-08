//
//  SelectionView.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 03.08.2024.
//

import SwiftUI

struct SelectionView: View {
    @State var title: String
    @State var desc: String
    
    var body: some View {
        VStack {
            Text("**\(title)**")
                .font(.system(size: 10.79))
                .foregroundStyle(.white)
            Text(desc)
                .foregroundStyle(.white)
                .multilineTextAlignment(.center)
                .font(.system(size: 9.45))
        }
        .padding(15)
        .background(Image("Selection"))
        .frame(width: 145.73, height: 60.05)
        .clipShape(RoundedRectangle(cornerRadius: 16.19))
    }
}

#Preview {
    SelectionView(title: "Завтраки вне дома", desc: "Куда сходить · Места")
}
