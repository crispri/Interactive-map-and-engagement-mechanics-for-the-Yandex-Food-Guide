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

extension CollectionDTO {
    static var mockData = [
        CollectionDTO(
            name: "Топ-50 ресторанов Москвы",
            description: "Лучшие рестораны по версии Анатолия",
            snippets: [0]
        ),
        CollectionDTO(
            name: "Топ-49 ресторанов Москвы",
            description: "Лучшие рестораны по версии Анатолия",
            snippets: [0]
        ),
        CollectionDTO(
            name: "Топ-48 ресторанов Москвы",
            description: "Лучшие рестораны по версии Анатолия",
            snippets: [0]
        ),
        CollectionDTO(
            name: "Топ-50 ресторанов Москвы",
            description: "Лучшие рестораны по версии Анатолия",
            snippets: [0]
        ),
        CollectionDTO(
            name: "Топ-49 ресторанов Москвы",
            description: "Лучшие рестораны по версии Анатолия",
            snippets: [0]
        ),
        CollectionDTO(
            name: "Топ-48 ресторанов Москвы",
            description: "Лучшие рестораны по версии Анатолия",
            snippets: [0]
        ),
    ]
}
