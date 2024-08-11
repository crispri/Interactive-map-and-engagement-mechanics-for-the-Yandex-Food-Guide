//
//  ISelectionsResponse.swift
//  iosApp
//
//  Created by Максим Кузнецов on 11.08.2024.
//

import Foundation

protocol ISelectionsResponse: Decodable {
    var items: [SelectionDTO] { get }
}
