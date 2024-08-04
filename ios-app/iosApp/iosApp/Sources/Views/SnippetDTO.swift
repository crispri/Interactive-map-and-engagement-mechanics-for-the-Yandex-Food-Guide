//
//  SelectionDTO.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 04.08.2024.
//

import Foundation

struct SnippetDTO: Codable {
    let id: Int64
    let lat: Double
    let lon: Double
    let name: String
    let description: String
    let address: String
    let approved: Bool
    let rating: Int64
    let check: Int64
    let priceLowerBound: Int64
    let priceUpperBound: Int64
    
    init(
        id: Int64,
        lat: Double,
        lon: Double,
        name: String,
        description: String,
        address: String,
        approver: Bool,
        rating: Int64,
        check: Int64,
        priceLowerBound: Int64,
        priceUpperBound: Int64
    ) {
        self.id = id
        self.lat = lat
        self.lon = lon
        self.name = name
        self.description = description
        self.address = address
        self.approved = approver
        self.rating = rating
        self.check = check
        self.priceLowerBound = priceLowerBound
        self.priceUpperBound = priceUpperBound
    }
}
