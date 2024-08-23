//
//  SnippetRequest.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 16.08.2024.
//

import Foundation

struct SnippetRequest: ISnippetRequest {
    let lowerLeftCorner: Point
    let topRightCorner: Point
    let onlyCollections: Bool
    let maxCount: Int64
    
    let filters: [FilterDTO]?
    
    private enum CodingKeys: String, CodingKey {
        case lowerLeftCorner = "lower_left_corner"
        case topRightCorner = "top_right_corner"
        case onlyCollections = "only_collections"
        case maxCount = "max_count"
        case filters
    }
}
