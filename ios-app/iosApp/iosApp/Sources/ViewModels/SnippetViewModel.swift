//
//  SelectionsViewModel.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 04.08.2024.
//

import SwiftUI

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
        eventPlaceUser()
        eventCenterCamera(to: .user)
        eventOnGesture()
    }
    
    func eventOnGesture() {
        Task { await fetchSnippets() }
        Task { await fetchSelections() }
    }
    
    func eventCenterCamera(to option: MapManager.CameraTargetOption) {
        mapManager.centerCamera(to: option)
    }
    
    func eventPlaceUser() {
        mapManager.placeUser()
    }
    
    func eventSelectionPressed(at index: Int, reader: ScrollViewProxy) async {
        if let selectedCollection,
           selectedCollection == collections[index] {
            self.selectedCollection = nil
            await fetchSnippets()
        } else {
            selectedCollection = collections[index]
            reader.scrollTo(index, anchor: .center)
            await fetchSelectionSnippets(id: selectedCollection?.id ?? "")
            eventCenterCamera(to: .pins)
        }
    }
    
    // MARK: Wrappers for fetching snippets and collections.
    
    func fetchSnippets() async {
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
        catch { print(error)  }
    }
    
    func fetchSelections() async {
        do {
            collections = try await loadSelections()
        } catch { print(error) }
    }
    
    func fetchSelectionSnippets(id: String) async {
        do {
            snippets = try await loadSelectionSnippets(id: id)
            mapManager.placePins(snippets)
        } catch { print(error) }
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
