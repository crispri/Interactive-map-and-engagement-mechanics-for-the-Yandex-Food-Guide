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
    
    @MainActor
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
                let rect = mapManager.getScreenPoints()
                let ll = rect.lowerLeftCorner
                let tr = rect.topRightCorner
                
                print("Square position: \(ll.description) \(tr.description)")
                if abs(ll.lat - tr.lat) > 0.1 {
                    mapManager.disablePins()
                    mapManager.cleanPins()
                    return
                }
                
                let restaurants = try await loadSnippets(
                    lowerLeftCorner: .init(lat: ll.lat, lon: ll.lon), topRightCorner: .init(lat: tr.lat, lon: tr.lon)
                )
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
