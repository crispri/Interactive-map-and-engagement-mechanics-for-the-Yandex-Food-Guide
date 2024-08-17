//
//  Filter.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 17.08.2024.
//

import Foundation

struct Tag: Identifiable {
    var id: String = UUID().uuidString
    let text: String
}
