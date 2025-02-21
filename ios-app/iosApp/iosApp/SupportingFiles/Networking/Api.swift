//
//  Configuration.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 04.08.2024.
//

import Foundation

public enum Api {
    static var baseURL: String {
        "http://51.250.39.97:8080/guide/v1/"
    }
    static var sessionID: String {
        "5142cece-b22e-4a4f-adf9-990949d053ff"
    }
    static var geocoderURL: String {
        "https://geocode-maps.yandex.ru/1.x/"
    }
    static var geocoderKey: String {
        "8468b494-d413-4abb-8175-919dad37a245"
    }
    
    case restaurants
    case restaurant(id: String)
    case selections
    case selection(id: String)
    case collection
    case collections(id: String)
    
    public var path: String {
        switch self {
        case .restaurants:
            return "restaurants"
        case .restaurant(id: let id):
            return "restaurants/\(id)"
        case .selections:
            return "selections"
        case .selection(id: let id):
            return "selections/\(id)"
        case .collection:
            return "collection"
        case .collections(id: let id):
            return "collection/\(id)"
        }
    }
}
