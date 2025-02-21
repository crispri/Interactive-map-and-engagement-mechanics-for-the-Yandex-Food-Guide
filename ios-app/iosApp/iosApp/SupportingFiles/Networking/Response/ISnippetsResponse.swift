//
//  ISnippetsResponse.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 04.08.2024.
//

import Foundation

protocol ISnippetsResponse: Codable {
    var items: [SnippetDTO] { get }
}
