//
//  SelectionDTO.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 04.08.2024.
//

import Foundation

struct SnippetDTO: Codable, Hashable, Equatable, Identifiable {
    let id: String
    let coordinates: Coordinates
    let name: String
    let description: String
    let address: String
    let isApproved: Bool
    let rating: Double
    let priceLowerBound: Int
    let priceUpperBound: Int
    let openTime: String
    let closeTime: String
    let tags: [String]
    let inCollection: Bool
    let food: String
    let interior: [String]
    let score: Int
    let additionalInfo: String

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
        openTime: String = "",
        closeTime: String = "",
        tags: [String] = [],
        inCollection: Bool = true,
        food: String = "",
        interior: [String] = [],
        score: Int = 0,
        additionalInfo: String = ""
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
        self.openTime = openTime
        self.closeTime = closeTime
        self.tags = tags
        self.inCollection = inCollection
        self.food = food
        self.interior = interior
        self.score = score
        self.additionalInfo = additionalInfo
    }
    
    private enum CodingKeys: String, CodingKey {
        case id, coordinates, name, description, address, rating, tags
        case isApproved = "is_approved"
        case priceLowerBound = "price_lower_bound"
        case priceUpperBound = "price_upper_bound"
        case openTime = "open_time"
        case closeTime = "close_time"
        case inCollection = "in_collection"
        case food, interior, score
        case additionalInfo = "additional_info"
    }
}

extension SnippetDTO {
    static var mockData = [
        SnippetDTO(
            id: "0",
            coordinates: Coordinates(lat: 55.74048502788512, lon: 37.610338866258985),
            name: "Сыроварня",
            description: "Крупная сеть ресторанов с собственным сырным производством",
            address: "Берсеневский пер., 2, стр.1, Москва, 119072",
            rating: 5,
            priceLowerBound: 1200,
            priceUpperBound: 6000
        ),
        SnippetDTO(
            id: "1",
            coordinates: Coordinates(lat: 55.755956, lon: 37.643203),
            name: "Blanc",
            description: "Ресторан авторской кухни, расположенный в исторической части города",
            address: "Хохловский переулок, 7-9с2",
            rating: 5,
            priceLowerBound: 1200,
            priceUpperBound: 6000
        ),
        SnippetDTO(
            id: "2",
            coordinates: Coordinates(lat: 55.762986, lon: 37.634945),
            name: "Lions Head",
            description: "Классический ирландский паб, который предлагает своим гостям широкий выбор напитков",
            address: "Мясницкая улица, 15",
            rating: 5,
            priceLowerBound: 1200,
            priceUpperBound: 6000
        ),
        SnippetDTO(
            id: "3",
            coordinates: Coordinates(lat: 55.804959, lon: 37.589506),
            name: "Ya Cafe",
            description: "Здесь вы найдете красивый зал, а также вкусное и разнообразное меню.",
            address: "Новодмитровская улица, 2к1",
            rating: 5,
            priceLowerBound: 1200,
            priceUpperBound: 6000
        )
    ]
}
