//
//  CollectionDTO.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 04.08.2024.
//

import Foundation

struct CollectionDTO: Codable, Identifiable {
    var id: String = UUID().uuidString
    let name: String
    let description: String
    let snippets: [Int64]
}
