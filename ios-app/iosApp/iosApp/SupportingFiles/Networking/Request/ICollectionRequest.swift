//
//  ICollectionRequest.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 19.08.2024.
//

import Foundation

protocol ICollectionRequest: Codable {
    var name: String { get }
    var description: String { get }
}
