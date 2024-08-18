//
//  UserCollection.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 18.08.2024.
//

import Foundation

struct UserCollection: Hashable, Identifiable, Equatable {
    let id = UUID().uuidString
    let selection: SelectionDTO
    var restaurants: [SnippetDTO] = []
}

extension UserCollection {
    static var mockData = [
        UserCollection(selection: SelectionDTO(name: "Хочу сходить", description: "", picture: "WantToGo"), restaurants: [SnippetDTO(inCollection: true)]),
        UserCollection(selection: SelectionDTO(name: "Хочу заказать", description: "", picture: "WantToOrder"), restaurants: [])
    ]
}
