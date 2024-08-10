//
//  BottomRestView.swift
//  iosApp
//
//  Created by Христина Буянова on 10.08.2024.
//

import SwiftUI

struct BottomRestView: View {
    var body: some View {
        HStack {
            Image(systemName: "location.north.circle.fill")
                .resizable()
                .frame(width: 25, height: 25)
            Text("До 02:00")
            Image(systemName: "circle.fill")
                .resizable()
                .frame(width: 2, height: 2)
            Text("534 м")
            Image(systemName: "circle.fill")
                .resizable()
                .frame(width: 2, height: 2)
            Text("16 мин на машине")
            Image(systemName: "chevron.right")
        }
    }
}

#Preview {
    BottomRestView()
}
