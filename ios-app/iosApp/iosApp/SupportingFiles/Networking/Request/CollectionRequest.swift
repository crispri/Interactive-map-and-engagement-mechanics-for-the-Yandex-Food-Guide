//
//  CollectionRequest.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 19.08.2024.
//

import Foundation

struct CollectionRequest: ICollectionRequest {
    let name: String
    let description: String
    
    private enum CodingKeys: String, CodingKey {
        case name, description
    }
}
