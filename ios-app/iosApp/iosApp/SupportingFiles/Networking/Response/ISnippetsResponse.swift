//
//  ISnippetsResponse.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 04.08.2024.
//

import Foundation

protocol ISnippetsResponse: Decodable {
    var items: [SnippetDTO] { get }
}
