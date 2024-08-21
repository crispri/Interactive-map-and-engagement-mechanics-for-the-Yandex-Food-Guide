//
//  SnippetViewModel.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 04.08.2024.
//

import SwiftUI
import CoreLocation
import BottomSheet
import OrderedCollections

@MainActor
final class SnippetViewModel: ObservableObject {
    private let MAX_POLIGON_WIDTH = 0.20
    
    @Published var isPinFocusMode = false
    @Published var sheetPosition: BottomSheetPosition = .dynamicBottom
    @Published var userLocaitonTitle = "Поиск геопозиции..."
    @Published var snippets = [SnippetDTO]()
    @Published var selections = [SelectionDTO]()
    @Published var selectedCollection: SelectionDTO?
    @Published var currentSelection: SelectionDTO?
    @Published var selectedPin: SnippetDTO? = nil
    
    @Published var userCollections: [UserCollection] = UserCollection.mockData
    @Published var onlyUserCollections: Bool = false
    @Published var currentRestaurantID: String?
    func addRestaurant(to collectionID: String, restaurantID: String) {
        if let index = userCollections.firstIndex(where: { $0.id == collectionID }) {
            objectWillChange.send()
            Task { try await putRestaurantTo(collectionID: userCollections[index].id, restaurantID: restaurantID) }
        }
    }
    
    func removeRestaurant(from collectionID: String, restaurantID: String) {
        objectWillChange.send()
    }
    
    func userCollectionsContains(selectionID: String) -> Bool {
        userCollections.map { uc in uc.selection.id }.contains(selectionID)
    }
    
    func toggleSelectionInUserCollections(selection: SelectionDTO) {
        if userCollectionsContains(selectionID: selection.id) {
            userCollections = userCollections.filter { $0.selection.id != selection.id }
        } else {
            userCollections.append(.init(selection: selection))
        }
    }
    
    @Published var filterCategories: [FilterCategory] = FilterDTO.mockData
    
    var mapManager: MapManager!
    private let networkManager = NetworkManager()

    private var filtersDTO: [FilterDTO] {
        let activeFilters = filterCategories.flatMap { $0.filters.filter { $0.isActive }}
        var activeFiltersDTOs = activeFilters.flatMap(\.dtos)
        
        // MARK: Adds current selection to filters
        if let currentSelection {
            activeFiltersDTOs.append(FilterDTO(
                    property: .selectionID,
                    operator: .in,
                    value: currentSelection.id
                ))
        } else {
            activeFiltersDTOs = activeFilters.flatMap(\.dtos)
        }
        
        return activeFiltersDTOs
    }
    
    init() {
        mapManager = MapManager(delegate: self)
    }
    init(selections: [SelectionDTO]) {
        self.selections = selections
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
        Task { await fetchUserCollections() }
        mapManager.map.isScrollGesturesEnabled = true
        mapManager.map.isRotateGesturesEnabled = true
        mapManager.map.isZoomGesturesEnabled = true
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
            if onlyUserCollections {
                selections = userCollections.map{ $0.selection }
            } else {
                selections = try await loadSelections()
            }
            
            let userLocation = try mapManager.getUserLocation()
            let screenPoints = mapManager.getScreenPoints()
            let params: [AnyHashable : Any]? = [
                "user_id": "iOS user id",
                "timestamp": Int(Date().timeIntervalSince1970),
                "current_lat":  userLocation.lat,
                "current_long": userLocation.lon,
                "user_lat": screenPoints.lowerLeftCorner.lat,
                "user_long": screenPoints.lowerLeftCorner.lon,
                "selections_array": selections.map { $0.id }
            ]
            MetricaManager.logEvent(
                name: "main_screen_button_open_map",
                params: params
            )
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
                switch selection.preCreatedCollectionName {
                case "Хочу сходить":
                    userCollections[0] = UserCollection(selection: selection, picture: "WantToGo")
                case "Хочу заказать":
                    userCollections[1] = UserCollection(selection: selection, picture: "WantToOrder")
                default:
                    if ["Хочу сходить", "Хочу заказать"].contains(selection.name) {
                        continue
                    }
                    userCollectionsTail.append(.init(selection: selection))
                }
            }
            userCollections = userCollections[0...1] + Array(OrderedSet(userCollectionsTail + userCollections[2...]))
            
        } catch { print(error) }
    }
    
    // MARK: load data from server.
    
    private func loadSnippets(lowerLeftCorner: Point, topRightCorner: Point) async throws -> [SnippetDTO] {
        let data = try await networkManager.fetchSnippets(lowerLeftCorner: lowerLeftCorner, topRightCorner: topRightCorner, filters: filtersDTO, onlyUserCollections: onlyUserCollections)
        return data
    }
    
    private func loadSelections() async throws -> [SelectionDTO] {
        let data = try await networkManager.fetchSelections()
        return data
    }
        
    private func loadSelectionSnippets(id: String) async throws -> [SnippetDTO] {
        let data = try await networkManager.fetchSnippets(lowerLeftCorner: .init(lat: 0, lon: 0), topRightCorner: .init(lat: 90, lon: 90), collectionID: id)
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
    
    func postCollection(name: String, description: String) async throws -> String {
        return try await networkManager.postCollection(name: name, description: description)
    }
    
    func putRestaurantTo(collectionID: String, restaurantID: String) async throws {
        try await networkManager.putRestaurantToCollection(collectionID: collectionID, restaurantID: restaurantID)
    }
}
