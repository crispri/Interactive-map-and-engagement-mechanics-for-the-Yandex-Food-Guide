//
//  CollectionDTO.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 04.08.2024.
//

import Foundation

struct SelectionDTO: Codable, Identifiable, Equatable, Hashable {
    let id: String
    let name: String
    let description: String
    let picture: String?
    let link: String?
    let preCreatedCollectionName: String?
    
    init(
        id: String = UUID().uuidString,
        name: String = "",
        description: String = "",
        picture: String? = "",
        link: String? = "",
        preCreatedCollectionName: String? = ""
    ) {
        self.id = id
        self.name = name
        self.description = description
        self.picture = picture
        self.link = link
        self.preCreatedCollectionName = preCreatedCollectionName
    }
    init(selection: SelectionDTO, picture: String) {
        self.id = selection.id
        self.name = selection.name
        self.description = selection.description
        self.picture = picture
        self.link = selection.link
        self.preCreatedCollectionName = selection.preCreatedCollectionName
    }
    
    private enum CodingKeys: String, CodingKey {
        case id, name, description, picture, link
        case preCreatedCollectionName = "pre_created_collection_name"
    }
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
