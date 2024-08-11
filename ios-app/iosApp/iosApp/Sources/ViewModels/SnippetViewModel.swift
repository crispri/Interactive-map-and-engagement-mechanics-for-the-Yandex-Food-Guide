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
    @Published var snippets = [SnippetDTO]()
    @Published var collections = [SelectionDTO]()
    @Published var selectedCollection: SelectionDTO? = nil
    
    var mapManager = MapManager()
    private let networkManager = NetworkManager()
    
    init() {
        mapManager.delegate = self
    }
    
    func eventOnAppear() {
        eventCenterCamera(to: .user)
        mapManager.placeUser()
        eventOnGesture()
    }
    
    func eventOnGesture() {
        fetchSnippets()
        fetchSelections()
    }
    
    func eventCenterCamera(to option: MapManager.CameraTargetOption) {
        mapManager.centerCamera(to: option)
    }
    
    // MARK: tasks for fetching snippets and collections.
    
    func fetchSnippets() {
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
    
    func fetchSelections() {
        Task {
            do {
                collections = try await loadSelections()
            } catch {
                print(error)
            }
        }
    }
    
    func fetchSelectionSnippets(id: String) {
        Task {
            do {
                snippets = try await loadSelectionSnippets(id: id)
                mapManager.placePins(snippets)
                print("🔥 \(snippets)")
            } catch {
                print(error)
            }
        }
    }
    
    // MARK: load data from server.
    
    private func loadSnippets(lowerLeftCorner: Point, topRightCorner: Point) async throws -> [SnippetDTO] {
        let data = try await networkManager.fetchSnippets(lowerLeftCorner: lowerLeftCorner, topRightCorner: topRightCorner)
        return data
    }
    
    private func loadSelections() async throws -> [SelectionDTO] {
        let data = try await networkManager.fetchSelections()
        return data
    }
    
    private func loadSelectionSnippets(id: String) async throws -> [SnippetDTO] {
        let data = try await networkManager.fetchSelectionSnippets(id: id)
        return data
    }
}
