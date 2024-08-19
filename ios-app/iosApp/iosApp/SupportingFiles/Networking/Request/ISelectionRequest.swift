//
//  ISelectionRequest.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 19.08.2024.
//

import Foundation

protocol ISelectionRequest: Codable {
    var returnCollections: Bool { get }
}
