//
//  SelectionsRequest.swift
//  iosApp
//
//  Created by Максим Кузнецов on 18.08.2024.
//

struct SelectionsRequest: Codable {
    let returnCollections: Bool
    
    private enum CodingKeys: String, CodingKey {
        case returnCollections = "return_collections"
    }
}
