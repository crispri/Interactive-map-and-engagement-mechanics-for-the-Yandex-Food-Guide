//
//  SelectionDTO.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 04.08.2024.
//

import Foundation

struct Coordinates: Codable, Hashable {
    let lat: Double
    let lon: Double
}

struct SnippetDTO: Codable, Hashable {
    let id: String
    let coordinates: Coordinates
    let name: String
    let description: String
    let address: String
    let isApproved: Bool
    let rating: Double
    let priceLowerBound: Int
    let priceUpperBound: Int
    let isFavorite: Bool
    let openTime: String
    let closeTime: String
    let tags: [Int]
    
    init(
        id: String = UUID().uuidString,
        coordinates: Coordinates = Coordinates(lat: 0.0, lon: 0.0),
        name: String = "",
        description: String = "",
        address: String = "",
        isApproved: Bool = true,
        rating: Double = 0.0,
        priceLowerBound: Int = 0,
        priceUpperBound: Int = 0,
        isFavorite: Bool = true,
        openTime: String = "",
        closeTime: String = "",
        tags: [Int] = []
    ) {
        self.id = id
        self.coordinates = coordinates
        self.name = name
        self.description = description
        self.address = address
        self.isApproved = isApproved
        self.rating = rating
        self.priceLowerBound = priceLowerBound
        self.priceUpperBound = priceUpperBound
        self.isFavorite = isFavorite
        self.openTime = openTime
        self.closeTime = closeTime
        self.tags = tags
    }
    
    private enum CodingKeys: String, CodingKey {
        case id, coordinates, name, description, address, rating, tags
        case isApproved = "is_approved"
        case priceLowerBound = "price_lower_bound"
        case priceUpperBound = "price_upper_bound"
        case isFavorite = "is_favorite"
        case openTime = "open_time"
        case closeTime = "close_time"
    }
}

extension SnippetDTO {
    static var mockData = [
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
            lat: 55.755956,
            lon: 37.643203,
            name: "Blanc",
            description: "Ресторан авторской кухни, расположенный в исторической части города",
            address: "Хохловский переулок, 7-9с2",
            approver: true,
            rating: 5,
            check: 9,
            priceLowerBound: 1200,
            priceUpperBound: 6000
        ),
        SnippetDTO(
            id: 2,
            lat: 55.762986,
            lon: 37.634945,
            name: "Lions Head",
            description: "Классический ирландский паб, который предлагает своим гостям широкий выбор напитков",
            address: "Мясницкая улица, 15",
            approver: true,
            rating: 5,
            check: 9,
            priceLowerBound: 1200,
            priceUpperBound: 6000
        ),
        SnippetDTO(
            id: 3,
            lat: 55.804959,
            lon: 37.589506,
            name: "Ya Cafe",
            description: "Здесь вы найдете красивый зал, а также вкусное и разнообразное меню.",
            address: "Новодмитровская улица, 2к1",
            approver: true,
            rating: 5,
            check: 9,
            priceLowerBound: 1200,
            priceUpperBound: 6000
        )
    ]
}
