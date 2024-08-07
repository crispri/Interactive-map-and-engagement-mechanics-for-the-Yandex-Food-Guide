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
