//
//  SelectionsViewModel.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 04.08.2024.
//

import Foundation

@MainActor
final class SnippetViewModel: ObservableObject {
    @Published var userLocaitonTitle = "<Здесь будет адрес>"
    @Published var snippets = SnippetDTO.mockData
    @Published var collections = SelectionDTO.mockData
    
    var mapManager = MapManager()
    private let networkManager = NetworkManager()
    
    init() {
        mapManager.delegate = self
    }
    
    func eventOnAppear() {
        eventCenterCamera(to: .user)
        eventOnGesture()
    }
    
    func eventOnGesture() {
        Task {
            do {
                let square = mapManager.getScreenPoints()
                
                print("\(square.lowerLeftCorner) \(square.topRightCorner)")
                
                let restaurants = try await loadSnippets(
                    lowerLeftCorner: Point(
                        lat: square.lowerLeftCorner.lat,
                        lon: square.lowerLeftCorner.lon
                    ),
                    topRightCorner: Point(
                        lat: square.topRightCorner.lat,
                        lon: square.topRightCorner.lon
                    )
                )
                
                for i in 0..<restaurants.count{ print(restaurants[i].address) }
                
                snippets = restaurants
                mapManager.placePins(restaurants)
            }
            catch {
                print(error)
            }
        }
    }
    
    func eventCenterCamera(to option: MapManager.CameraTargetOption) {
        mapManager.centerCamera(to: option)
    }
    
    public func loadSnippets(lowerLeftCorner: Point, topRightCorner: Point) async throws -> [SnippetDTO] {
        let data = try await networkManager.fetchSnippets(lowerLeftCorner: lowerLeftCorner, topRightCorner: topRightCorner)
        return data
    }
}
