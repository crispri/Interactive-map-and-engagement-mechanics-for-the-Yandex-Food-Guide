//
//  SelectionRequest.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 18.08.2024.
//

import Foundation

struct SelectionRequest: ISelectionRequest {
    let returnCollections: Bool
    
    enum CodingKeys: String, CodingKey {
        case returnCollections = "return_collections"
    }
}
