//
//  GetSnippetsRequestModel.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 04.08.2024.
//

import Foundation

struct SnippetsRequest: Codable {
    let lowerLeftCorner: Point
    let topRightCorner: Point
    let maxCount: Int64
    
    private enum CodingKeys: String, CodingKey {
        case lowerLeftCorner = "lower_left_corner"
        case topRightCorner = "top_right_corner"
        case maxCount = "max_count"
    }
}
