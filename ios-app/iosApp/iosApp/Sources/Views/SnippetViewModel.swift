//
//  SelectionsViewModel.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 04.08.2024.
//

import Foundation

final class SnippetViewModel: ObservableObject {
    @Published var userLocaitonTitle = "<Здесь будет адрес>"
    @Published var snippets = SnippetDTO.mockData
    @Published var collections = CollectionDTO.mockData
    
    @Published var mapManager = MapManager()
    private let networkManager = NetworkManager()
    
    func eventOnAppear() {
        eventFetchUserLocation()
        mapManager.placePins(snippets)
        mapManager.centerCamera(to: .pins)
    }
    
    func eventFetchUserLocation() {
        mapManager.centerCamera(to: .user)
    }
    
    public func loadSnippets(lowerLeftCorner: Point, topRightCorner: Point) async throws -> [SnippetDTO] {
        return try await networkManager.fetchSnippets(from: lowerLeftCorner, to: topRightCorner)
    }
}
