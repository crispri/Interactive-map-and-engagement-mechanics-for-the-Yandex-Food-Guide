//
//  Filter.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 16.08.2024.
//

import Foundation


struct FilterDTO: Encodable {
    enum Property: String, Encodable {
        case rating = "rating"
        case priceLowerBound = "price_lower_bound"
        case priceUpperBound = "price_upper_bound"
        case openTime = "open_time"
        case closeTime = "close_time"
    }
    
    enum Operator: String, Encodable {
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
