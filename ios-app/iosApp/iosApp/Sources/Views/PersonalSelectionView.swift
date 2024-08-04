//
//  PersonalSelectionView.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 03.08.2024.
//

import SwiftUI

struct PersonalSelectionView: View {
    var body: some View {
        VStack {
            Text("**Собрали для вас**")
                .font(.system(size: 10.79))
                .foregroundStyle(.white)
            Text("Рекомендации от наших экспертов")
                .font(.system(size: 9.45))
                .foregroundStyle(.gray)
                .multilineTextAlignment(.center)
        }
        .background(Image("PersonalSelection"))
        .frame(width: 145.73, height: 60.05)
        .clipShape(RoundedRectangle(cornerRadius: 16.19))
    }
}

#Preview {
    PersonalSelectionView()
}
