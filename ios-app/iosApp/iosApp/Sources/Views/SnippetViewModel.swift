//
//  SelectionsViewModel.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 04.08.2024.
//

import Foundation

final class SnippetViewModel: ObservableObject {
    var mapManager = MapManager()
    @Published var userLocaitonTitle = "2-я Брестская, 1/5"
    
    @Published var snippets: [SnippetDTO] = [
        SnippetDTO(
            id: 0,
            lat: 55.74048502788512,
            lon: 37.610338866258985,
            name: "Сыроварня",
            description: "Крупная сеть ресторанов с собственным сырным производством",
            address: "Берсеневский пер., 2, стр.1, Москва, 119072",
            approver: true,
            rating: 5,
            check: 9,
            priceLowerBound: 1200,
            priceUpperBound: 6000
        ),
        SnippetDTO(
            id: 1,
            lat: 55.74048502788512,
            lon: 37.610338866258985,
            name: "Blanc",
            description: "Ресторан авторской кухни, расположенный в исторической части города",
            address: "м. Китай-город",
            approver: true,
            rating: 5,
            check: 9,
            priceLowerBound: 1200,
            priceUpperBound: 6000
        ),
        SnippetDTO(
            id: 2,
            lat: 55.74048502788512,
            lon: 37.610338866258985,
            name: "Lions Head",
            description: "Классический ирландский паб, который предлагает своим гостям широкий выбор напитков",
            address: "м. Тургеневская",
            approver: true,
            rating: 5,
            check: 9,
            priceLowerBound: 1200,
            priceUpperBound: 6000
        ),
        SnippetDTO(
            id: 3,
            lat: 55.74048502788512,
            lon: 37.610338866258985,
            name: "Ya Cafe",
            description: "Классический ирландский паб, который предлагает своим гостям широкий выбор напитков",
            address: "м. Тургеневская",
            approver: true,
            rating: 5,
            check: 9,
            priceLowerBound: 1200,
            priceUpperBound: 6000
        )
    ]
    
    @Published var collections: [CollectionDTO] = [
        CollectionDTO(
            name: "Топ-50 ресторанов Москвы",
            description: "Лучшие рестораны по версии Анатолия",
            snippets: [0]
        ),
        CollectionDTO(
            name: "Топ-49 ресторанов Москвы",
            description: "Лучшие рестораны по версии Анатолия",
            snippets: [0]
        ),
        CollectionDTO(
            name: "Топ-48 ресторанов Москвы",
            description: "Лучшие рестораны по версии Анатолия",
            snippets: [0]
        ),
        CollectionDTO(
            name: "Топ-50 ресторанов Москвы",
            description: "Лучшие рестораны по версии Анатолия",
            snippets: [0]
        ),
        CollectionDTO(
            name: "Топ-49 ресторанов Москвы",
            description: "Лучшие рестораны по версии Анатолия",
            snippets: [0]
        ),
        CollectionDTO(
            name: "Топ-48 ресторанов Москвы",
            description: "Лучшие рестораны по версии Анатолия",
            snippets: [0]
        ),
    ]
    
    func eventOnAppear() {
        eventFetchUserLocation()
        mapManager.addPoints()
    }
    
    func eventFetchUserLocation() {
        mapManager.currentUserLocation()
    }

    private let networkManager = NetworkManager()
    
    public func loadSnippets(lowerLeftCorner: Point, topRightCorner: Point) async throws -> [SnippetDTO] {
        return try await networkManager.fetchSnippets(from: lowerLeftCorner, to: topRightCorner)
    }
}
