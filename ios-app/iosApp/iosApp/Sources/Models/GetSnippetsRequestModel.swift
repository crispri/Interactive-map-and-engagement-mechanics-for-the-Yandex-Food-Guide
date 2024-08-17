//
//  GetSnippetsRequestModel.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 04.08.2024.
//

import Foundation

protocol Printable {
    var description: String { get }
}

struct Point: Codable, Printable {
    var description: String {
        return "\(String(format: "%.1f", lat)) \(String(format: "%.1f", lon))"
    }
    
    let lat: Double
    let lon: Double
}

struct GetSnippetsRequestModel: Codable {
    let lowerLeftCorner: Point
    let topRightCorner: Point
    let maxCount: Int64
    
    private enum CodingKeys: String, CodingKey {
        case lowerLeftCorner = "lower_left_corner"
        case topRightCorner = "top_right_corner"
        case maxCount = "max_count"
    }
}
