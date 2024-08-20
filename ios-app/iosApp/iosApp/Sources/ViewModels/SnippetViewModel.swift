//
//  SnippetViewModel.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 04.08.2024.
//

import SwiftUI
import CoreLocation
import BottomSheet

@MainActor
final class SnippetViewModel: ObservableObject {
    private let MAX_POLIGON_WIDTH = 0.20
    
    @Published var isPinFocusMode = false
    @Published var sheetPosition: BottomSheetPosition = .dynamicBottom
    @Published var userLocaitonTitle = "Поиск геопозиции..."
    @Published var snippets = [SnippetDTO]()
    @Published var selections = [SelectionDTO]()
    @Published var selectedCollection: SelectionDTO? = nil
    @Published var currentSelection: SelectionDTO?
    @Published var selectedPin: SnippetDTO? = nil
    
    @Published var userCollections = UserCollection.mockData
    
    @Published var filterCategories: [FilterCategory] = FilterDTO.mockData
    private var filtersDTO: Array<FilterDTO> {
        let activeFilters = filterCategories.flatMap { $0.filters.filter { $0.isActive }}
        return activeFilters.flatMap(\.dtos)
    }
    
    var mapManager: MapManager!
    private let networkManager = NetworkManager()
    
    init() {
        mapManager = MapManager(delegate: self)
        for index in userCollections.indices {
            Task {
                let id = try await sendUserCollection(
                    name: userCollections[index].selection.name,
                    description: userCollections[index].selection.description
                )
                userCollections[index].id = id
            }
        }
    }
    
    func eventOnAppear() {
        eventPlaceUser()
        eventCenterCamera(to: .user)
        eventOnGesture()
        Task { await fetchSelections() }
    }

    func eventOnAppearForMain() {
        eventCenterCamera(to: .user)
        eventOnGesture()
        mapManager.map.isScrollGesturesEnabled = false
        mapManager.map.isRotateGesturesEnabled = false
        mapManager.map.isZoomGesturesEnabled = false
    }

    func eventOnGesture() {
        Task { await fetchSnippets() }
        Task { await fetchSelections() }
        Task { try? await loadAddress() }
        
        eventCenterCamera(to: .user)
        mapManager.placeUser()
        onCameraMove()
    }
    
    @MainActor
    func onCameraMove() {
        guard !mapManager.isPinFocusMode else { return }
        Task {
            do {
                await fetchSelections()
                let rect = mapManager.getScreenPoints()
                let ll = rect.lowerLeftCorner
                let tr = rect.topRightCorner
                
                print("Square position: \(ll.description) \(tr.description)")
                
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
    
    func eventCenterCamera(to option: MapManager.CameraTargetOption) {
        mapManager.centerCamera(to: option)
    }
    
    func eventPlaceUser() {
        mapManager.placeUser()
    }
    
    func eventSelectionPressed(at index: Int, reader: ScrollViewProxy) async {
        if let currentSelection,
           currentSelection == selections[index] {
            self.currentSelection = nil
            await fetchSnippets()
        } else {
            currentSelection = selections[index]
            reader.scrollTo(index, anchor: .center)
            await fetchSelectionSnippets(id: currentSelection?.id ?? "")
            eventCenterCamera(to: .pins)
        }
    }
    
    func pinFocusModeEnabled(_ isEnabled: Bool) {
        isPinFocusMode = isEnabled
        sheetPosition = isEnabled ? .absolute(500) : .dynamicBottom
    }
    
    func eventSnippetAppeared(_ snippet: SnippetDTO) {
        mapManager.eventSnippetAppeared(snippet)
    }
    
    // MARK: Wrappers for fetching snippets and selections.
    
    func fetchSnippets() async {
        do {
            let rect = mapManager.getScreenPoints()
            let ll = rect.lowerLeftCorner
            let tr = rect.topRightCorner
            
            print("Square position: \(ll.description) \(tr.description)")
            if abs(ll.lat - tr.lat) > MAX_POLIGON_WIDTH {
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
            selections = try await loadSelections()
        } catch { print(error) }
    }
        
    func fetchSelectionSnippets(id: String) async {
        do {
            snippets = try await loadSelectionSnippets(id: id)
            mapManager.placePins(snippets)
        } catch { print(error) }
    }
    
    func fetchUserCollections() async {
        do {
            let remoteUserCollections = try await loadUserCollections()
            var userCollectionsTail: [UserCollection] = []
            for selection in remoteUserCollections {
                let restaurants = try await loadSelectionSnippets(id: selection.id)
                
                userCollectionsTail += [UserCollection(
                    selection: selection,
                    restaurantIDs: Set(restaurants.map { restaurant in restaurant.id })
                )]
            }
            userCollections = UserCollection.mockData + userCollectionsTail
        } catch { print(error) }
    }
    
    // MARK: load data from server.
    
    private func loadSnippets(lowerLeftCorner: Point, topRightCorner: Point) async throws -> [SnippetDTO] {
        let data = try await networkManager.fetchSnippets(lowerLeftCorner: lowerLeftCorner, topRightCorner: topRightCorner, filters: filtersDTO)
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
    
    private func loadUserCollections() async throws -> [SelectionDTO] {
        let data = try await networkManager.fetchUserCollections()
        return data
    }

    func loadAddress() async throws {
    do {
        let userLocation = try mapManager.getUserLocation()
        
        let data = try await networkManager.fetchAddress(loc: userLocation)
        do {
            let decoder = JSONDecoder()
            let geocoderResponse = try decoder.decode(GeocoderResponse.self, from: data)
            let address = geocoderResponse.response.geoObjectCollection.featureMember.first?.geoObject.name ?? "Неизвестный адрес"
            print(address)
            userLocaitonTitle = address
        } catch {
            print("Ошибка декодирования: \(error)")
        }
    }
    catch {
        userLocaitonTitle = "Ошибка геолокации"
    }
}
    
    // MARK: send data to server
    
    func sendUserCollection(name: String, description: String) async throws -> String {
        return try await networkManager.sendCollection(name: name, description: description)
    }
}
