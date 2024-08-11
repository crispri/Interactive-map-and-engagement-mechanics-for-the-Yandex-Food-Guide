//
//  SelectionsViewModel.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 04.08.2024.
//

import Foundation
import CoreLocation

@MainActor
final class SnippetViewModel: ObservableObject {
    @Published var userLocaitonTitle = "Поиск геопозиции..."
    @Published var snippets = SnippetDTO.mockData
    @Published var collections = SelectionDTO.mockData
    
    var mapManager = MapManager()
    private let networkManager = NetworkManager()
    
    init() {
        mapManager.delegate = self
    }
    
    func eventOnAppear() {
        Task {
            try? await loadAddress()
        }
        
        eventCenterCamera(to: .user)
        mapManager.placeUser()
        onCameraMove()
    }
    
    @MainActor
    func onCameraMove() {
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
    
    func eventCenterCamera(to option: MapManager.CameraTargetOption) {
        mapManager.centerCamera(to: option)
    }
    
    public func loadSnippets(lowerLeftCorner: Point, topRightCorner: Point) async throws -> [SnippetDTO] {
        let data = try await networkManager.fetchSnippets(lowerLeftCorner: lowerLeftCorner, topRightCorner: topRightCorner)
        return data
    }
    
    public func loadAddress() async throws {
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
}
