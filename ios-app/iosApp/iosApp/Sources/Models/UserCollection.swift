//
//  UserCollection.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 18.08.2024.
//

import Foundation

struct UserCollection: Hashable, Identifiable, Equatable {
    var selection: SelectionDTO
    var id: String { return selection.id }
    var name: String { return selection.name }
    var picture: String { return selection.picture ?? "PlaceHolder" }
    var restaurantIDs: Set<String> {
        get async {
            do {
                return try await loadRestaurants()
            } catch {
                print(error)
                return []
            }
        }
    }
    var count: Int { get async {
        print("id \(id)")
        let a = await restaurantIDs.count
        print("count \(a)")
        return a
//        await restaurantIDs.count
    } }
    
    init(selection: SelectionDTO) {
        self.selection = selection
    }
    init(selection: SelectionDTO, picture: String) {
        self.selection = .init(selection: selection, picture: picture)
    }
    
    func contains(_ restaurantID: String) async -> Bool { await restaurantIDs.contains(restaurantID) }
    
    private func loadRestaurants() async throws -> Set<String> {
        let networkManager = NetworkManager()
        print(selection.id)
        let fetchedIDs = try await networkManager.fetchSnippets(
            lowerLeftCorner: .init(lat: 0, lon: 0),
            topRightCorner: .init(lat: 90, lon: 90),
            filters: [FilterDTO(property: .selectionID, operator: .in, value: selection.id)],
            onlyUserCollections: false
        ).map { $0.id }
        return Set(fetchedIDs)
    }
}

extension UserCollection {
    static var mockData = [
        UserCollection(selection: SelectionDTO(name: "Хочу сходить", description: "", picture: "WantToGo")),
        UserCollection(selection: SelectionDTO(name: "Хочу заказать", description: "", picture: "WantToOrder"))
    ]
}
