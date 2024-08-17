//
//  FilteredSnippetRequest.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 16.08.2024.
//

import Foundation

struct FilteredSnippetRequest: ISnippetRequest {
    let lowerLeftCorner: Point
    let topRightCorner: Point
    let maxCount: Int64
    
    let filters: [FilterDTO]?
    
    private enum CodingKeys: String, CodingKey {
        case lowerLeftCorner = "lower_left_corner"
        case topRightCorner = "top_right_corner"
        case maxCount = "max_count"
        case filters
    }
}
