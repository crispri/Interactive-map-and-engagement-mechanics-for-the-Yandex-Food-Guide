//
//  GrabberView.swift
//  iosApp
//
//  Created by Максим Кузнецов on 13.08.2024.
//

import SwiftUI

struct GrabberView: View {
    var body: some View {
        Capsule()
            .fill(Color.secondary)
            .opacity(0.5)
            .frame(width: 35, height: 5)
            .padding(6)
    }
}

#Preview {
    GrabberView()
}
