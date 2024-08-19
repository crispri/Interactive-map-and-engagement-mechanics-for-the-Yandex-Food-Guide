//
//  ISnippetRequest.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 16.08.2024.
//

import Foundation

protocol ISnippetRequest: Codable {
    var lowerLeftCorner: Point { get }
    var topRightCorner: Point { get }
    var onlyCollections: Bool { get }
    var maxCount: Int64 { get }
}
