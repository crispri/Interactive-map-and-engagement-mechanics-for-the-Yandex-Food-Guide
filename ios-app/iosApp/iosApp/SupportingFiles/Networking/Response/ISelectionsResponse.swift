//
//  ISelectionsResponse.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 16.08.2024.
//

import Foundation

protocol ISelectionsResponse: Decodable {
    var items: [SelectionDTO] { get }
}
