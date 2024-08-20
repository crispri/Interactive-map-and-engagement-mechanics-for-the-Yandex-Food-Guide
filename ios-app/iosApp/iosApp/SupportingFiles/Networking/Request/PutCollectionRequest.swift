//
//  PutCollectionRequest.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 20.08.2024.
//

import Foundation

struct PutCollectionRequest: IPutCollectionRequest {
    let restaurantID: String
    private enum CodingKeys: String, CodingKey {
        case restaurantID = "restaurant_id"
    }
}
