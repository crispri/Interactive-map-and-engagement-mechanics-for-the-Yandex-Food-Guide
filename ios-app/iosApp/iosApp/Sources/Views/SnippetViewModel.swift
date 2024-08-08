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
    @Published var collections = CollectionDTO.mockData
    
    @Published var mapManager = MapManager()
    private let networkManager = NetworkManager()
    
    func eventOnAppear() {
        eventCenterCamera(to: .user)
        Task {
            print("Task started")
            
            do {
                let res = try await loadSnippets(
                    lowerLeftCorner: Point(
                        lat: 55.582003,
                        lon: 37.363641
                    ),
                    topRightCorner: Point(
                        lat: 55.895010,
                        lon: 37.852533
                    )
                )
                
                for i in 0..<res.count{
                    print(
                        res[i].address
                    )}
                print("Task finished")
            }
            catch {
                print(error)
            }
            print("request sent")
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
