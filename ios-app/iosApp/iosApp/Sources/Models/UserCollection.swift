//
//  UserCollection.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 18.08.2024.
//

import Foundation

struct UserCollection: Hashable, Identifiable, Equatable {
    var id = UUID().uuidString
    let selection: SelectionDTO
    
    var restaurantIDs: Set<String>
    
    var count: Int {
        restaurantIDs.count
    }
    func contains(_ restaurantID: String) -> Bool {
        return restaurantIDs.contains(restaurantID)
    }
}

extension UserCollection {
    static var mockData = [
        UserCollection(selection: SelectionDTO(name: "Хочу сходить", description: "", picture: "WantToGo"), restaurantIDs: []),
        UserCollection(selection: SelectionDTO(name: "Хочу заказать", description: "", picture: "WantToOrder"), restaurantIDs: [])
    ]
}
