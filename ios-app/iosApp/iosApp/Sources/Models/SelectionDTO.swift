//
//  CollectionDTO.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 04.08.2024.
//

import Foundation

struct SelectionDTO: Codable, Identifiable, Equatable, Hashable {
    var id: String = UUID().uuidString
    let name: String
    let description: String
    let picture: String
}

extension SelectionDTO {
    static var mockData = [
        SelectionDTO(
            name: "Топ-50 ресторанов Москвы",
            description: "Лучшие рестораны по версии Анатолия",
            picture: ""
        ),
        SelectionDTO(
            name: "Топ-49 ресторанов Москвы",
            description: "Лучшие рестораны по версии Анатолия",
            picture: ""
        ),
        SelectionDTO(
            name: "Топ-48 ресторанов Москвы",
            description: "Лучшие рестораны по версии Анатолия",
            picture: ""
        ),
        SelectionDTO(
            name: "Топ-50 ресторанов Москвы",
            description: "Лучшие рестораны по версии Анатолия",
            picture: ""
        ),
        SelectionDTO(
            name: "Топ-49 ресторанов Москвы",
            description: "Лучшие рестораны по версии Анатолия",
            picture: ""
        ),
        SelectionDTO(
            name: "Топ-48 ресторанов Москвы",
            description: "Лучшие рестораны по версии Анатолия",
            picture: ""
        )
    ]
}
