//
//  Point.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 16.08.2024.
//

import Foundation

struct Point: Codable, Printable {
    var description: String {
        return "\(String(format: "%.1f", lat)) \(String(format: "%.1f", lon))"
    }
    
    let lat: Double
    let lon: Double
}
