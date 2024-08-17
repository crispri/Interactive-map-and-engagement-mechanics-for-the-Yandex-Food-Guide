//
//  Filter.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 16.08.2024.
//

import Foundation


struct FilterDTO: Codable, Hashable {
    enum Property: String, Codable {
        case rating = "rating"
        case priceLowerBound = "price_lower_bound"
        case priceUpperBound = "price_upper_bound"
        case openTime = "open_time"
        case closeTime = "close_time"
        case tags = "tags"
    }
    
    enum Operator: String, Codable {
        case lessThan = "lt"
        case greaterThan = "gt"
        case lessThanOrEqual = "le"
        case greaterThanOrEqual = "ge"
        case `in` = "in"
        case notIn = "notin"
        case equal = "eq"
        case notEqual = "neq"
    }
    
    let property: Property
    let `operator`: Operator
    let value: [String]
        
    init(property: Property, operator: Operator, value: String) {
        self.property = property
        self.operator = `operator`
        self.value = [value]
    }
    
    init(property: Property, operator: Operator, values: [String]) {
        self.property = property
        self.operator = `operator`
        self.value = values
    }
}

struct FilterCategory: Identifiable {
    var id = UUID().uuidString
    let title: String
    var filters: [Filter]
}

struct Filter: Identifiable, Hashable {
    static func == (lhs: Filter, rhs: Filter) -> Bool {
        lhs.id == rhs.id
    }
    
    var id = UUID().uuidString
    let name: String
    let dtos: [FilterDTO]
    var isActive: Bool
}

extension FilterDTO {
    static var mockData: [FilterCategory] = [
        FilterCategory(title: "", filters: [
            Filter(name: "Высокий рейтинг", dtos: [
                FilterDTO(property: .rating, operator: .greaterThanOrEqual, value: "4.5")
            ], isActive: false),
            Filter(name: "ULTIMA GUIDE", dtos: [
                FilterDTO(property: .tags, operator: .in, value: "ULTIMA GUIDE")
            ], isActive: false),
            Filter(name: "Открытая кухня", dtos: [
                FilterDTO(property: .tags, operator: .in, value: "Открытая кухня")
            ], isActive: false)
        ]),
        
        FilterCategory(title: "Время работы", filters: [
            Filter(name: "Открыто сейчас", dtos: [
                FilterDTO(property: .openTime, operator: .lessThanOrEqual, value: "\(DateFormatter.getTime(date: Date()))"),
                FilterDTO(property: .closeTime, operator: .greaterThanOrEqual, value: "\(DateFormatter.getTime(date: Date()))")
            ], isActive: false),
            Filter(name: "Круглосуточно", dtos: [
                FilterDTO(property: .openTime, operator: .greaterThanOrEqual, value: "00:00:00"),
                FilterDTO(property: .closeTime, operator: .lessThanOrEqual, value: "00:00:00"),
            ], isActive: false)
        ]),
        
        FilterCategory(title: "Тип заведения", filters: [
            Filter(name: "Бар", dtos: [
                FilterDTO(property: .tags, operator: .in, value: "Бар")
            ], isActive: false),
            Filter(name: "Ресторан", dtos: [
                FilterDTO(property: .tags, operator: .in, value: "Ресторан")
            ], isActive: false)
        ]),
        
        FilterCategory(title: "Повод", filters: [
            Filter(name: "Завтрак", dtos: [
                FilterDTO(property: .tags, operator: .in, value: "Завтрак")
            ], isActive: false),
            Filter(name: "Бизнес-ланч", dtos: [
                FilterDTO(property: .tags, operator: .in, value: "Бизнес-ланч")
            ], isActive: false),
            Filter(name: "Выпить кофе", dtos: [
                FilterDTO(property: .tags, operator: .in, value: "Выпить кофе")
            ], isActive: false),
            Filter(name: "Семейный ужин с детьми", dtos: [
                FilterDTO(property: .tags, operator: .in, value: "Семейный ужин с детьми")
            ], isActive: false)
        ])
    ]
}

extension DateFormatter {
    static func getTime(date: Date) -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "HH:mm:ss"
        
        return dateFormatter.string(from: date)
    }
}
