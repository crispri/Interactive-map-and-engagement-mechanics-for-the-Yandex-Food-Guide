//
//  SelectionDTO.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 04.08.2024.
//

import Foundation

struct SnippetDTO: Codable, Hashable {
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
