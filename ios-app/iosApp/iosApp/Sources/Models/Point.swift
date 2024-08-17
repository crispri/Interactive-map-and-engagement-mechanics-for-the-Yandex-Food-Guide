//
//  Point.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 16.08.2024.
//

import Foundation

struct Point: Codable, Printable {
    let lat: Double
    let lon: Double
    
    var description: String {
        return "\(String(format: "%.2f", lat)) \(String(format: "%.2f", lon))"
    }
}
