//
//  IPutCollectionRequest.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 20.08.2024.
//

import Foundation

protocol IPutCollectionRequest: Codable {
    var restaurantID: String { get }
}
