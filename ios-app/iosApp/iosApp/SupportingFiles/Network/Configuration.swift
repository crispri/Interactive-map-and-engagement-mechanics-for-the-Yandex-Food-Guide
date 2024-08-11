//
//  Configuration.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 04.08.2024.
//

import Foundation

struct Configuration {
    static var baseURL: String {
        "http://51.250.39.97:8080/guide/v1/"
    }
    static var token: String {
        "Yavanna"
    }
    static var geocoderURL: String {
        "https://geocode-maps.yandex.ru/1.x/"
    }
    static var geocoderKey: String {
        "8468b494-d413-4abb-8175-919dad37a245"
    }
}
