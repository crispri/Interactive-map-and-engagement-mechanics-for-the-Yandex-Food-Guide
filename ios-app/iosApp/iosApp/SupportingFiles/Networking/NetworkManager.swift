//
//  NetworkManager.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 04.08.2024.
//

import Foundation

final class NetworkManager {
    private let baseURL = URL(string: Api.baseURL)
    private let sessionID = Api.sessionID
    
    private func makeRequest(path api: Api, method: HTTPMethod) throws -> URLRequest {
        guard let url = URL(string: api.path, relativeTo: baseURL) else {
            throw URLError(.badURL)
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = method.rawValue
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.setValue("session_id=\(sessionID)", forHTTPHeaderField: "Cookie")
          
        return request
    }
    
    private func makeRequest(path: Api, method: HTTPMethod, with requestModel: Codable?) throws -> URLRequest {
        var request = try makeRequest(path: path, method: method)
        
        if let requestModel {
            let encoder = JSONEncoder()
            encoder.outputFormatting = .prettyPrinted
            let jsonData = try encoder.encode(requestModel)
            request.httpBody = jsonData
        }
          
        return request
    }
    
    private func performRequest<T: Decodable>(request: URLRequest) async throws -> T {
        let (data, response) = try await URLSession.shared.data(for: request)
        
        guard let httpResponse = response as? HTTPURLResponse else {
            throw URLError(.badServerResponse)
        }
        
        guard (200...299).contains(httpResponse.statusCode) else {
            throw NetworkingError.serverError(statucCode: httpResponse.statusCode)
        }
        
        let decodedResponse = try JSONDecoder().decode(T.self, from: data)
        
        return decodedResponse
    }
    
    private func performRequest(request: URLRequest) async throws {
        let (_, response) = try await URLSession.shared.data(for: request)
        
        guard let httpResponse = response as? HTTPURLResponse else {
            throw URLError(.badServerResponse)
        }
        
        guard (200...299).contains(httpResponse.statusCode) else {
            throw NetworkingError.serverError(statucCode: httpResponse.statusCode)
        }
    }
    
    func fetchSnippets(lowerLeftCorner: Point, topRightCorner: Point, filters: [FilterDTO]?, onlyUserCollections: Bool) async throws -> [SnippetDTO] {
        let request = try makeRequest(
            path: .restaurants,
            method: .post,
            with: SnippetRequest(
                lowerLeftCorner: lowerLeftCorner,
                topRightCorner: topRightCorner,
                onlyCollections: onlyUserCollections,
                maxCount: 0,
                filters: filters
            )
        )
        
        let data: SnippetsResponse = try await performRequest(request: request)
        
        return data.items
    }
    
    func fetchSnippets(lowerLeftCorner: Point, topRightCorner: Point, collectionID: String) async throws -> [SnippetDTO] {
        let request = try makeRequest(
            path: .restaurants,
            method: .post,
            with: SnippetRequest(
                lowerLeftCorner: lowerLeftCorner,
                topRightCorner: topRightCorner,
                onlyCollections: true,
                maxCount: 0,
                filters: [FilterDTO(property: .selectionID, operator: .in, value: collectionID)]
            )
        )
        
        let data: SnippetsResponse = try await performRequest(request: request)
        
        return data.items
    }
    
    func fetchSnippet(id: String) async throws -> SnippetDTO {
        let request = try makeRequest(
            path: .restaurant(id: id),
            method: .get
        )
        
        let data: SnippetDTO = try await performRequest(request: request)
        
        return data
    }
    
    func fetchSelections() async throws -> [SelectionDTO] {
        let request = try makeRequest(
            path: .selections,
            method: .post,
            with: SelectionRequest(returnCollections: false)
        )
        
        let data: SelectionsResponse = try await performRequest(request: request)
        
        return data.items
    }
    
    func fetchSelectionSnippets(id: String) async throws -> [SnippetDTO] {
        let request = try makeRequest(
            path: .selection(id: id),
            method: .get
        )
        
        let data: SnippetsResponse = try await performRequest(request: request)
        
        return data.items
    }
    
    func fetchUserCollections() async throws -> [SelectionDTO] {
        let request = try makeRequest(
            path: .selections,
            method: .post,
            with: SelectionRequest(returnCollections: true)
        )
        
        let data: SelectionsResponse = try await performRequest(request: request)
        
        return data.items
    }

    func fetchAddress(loc: Point) async throws -> Data {
        var components = URLComponents(string: "\(Api.geocoderURL)")!

        components.queryItems = [
            URLQueryItem(name: "apikey", value: "\(Api.geocoderKey)"),
            URLQueryItem(name: "geocode", value: "\(loc.lat),\(loc.lon)"),
            URLQueryItem(name: "lang", value: "ru_RU"),
            URLQueryItem(name: "sco", value: "latlong"),
            URLQueryItem(name: "kind", value: "house"),
            URLQueryItem(name: "format", value: "json"),
            URLQueryItem(name: "results", value: "1")
        ]
        
        guard let url = components.url else {
            throw URLError(.badURL)
        }
        
        var request = URLRequest(url: url)
        
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let (data, response) = try await URLSession.shared.data(for: request)
        
        guard let httpResponse = response as? HTTPURLResponse else {
            throw URLError(.badServerResponse)
        }
        
        guard (200...299).contains(httpResponse.statusCode) else {
            throw NetworkingError.serverError(statucCode: httpResponse.statusCode)
        }
        
        return data
    }
    
    func postCollection(name: String, description: String) async throws -> String {
        let request = try makeRequest(
            path: .collection,
            method: .post,
            with: CollectionRequest(
                name: name,
                description: description
            )
        )
        
        let data: CollectionResponse = try await performRequest(request: request)
        
        return data.id
    }
    
    func putRestaurantToCollection(collectionID: String, restaurantID: String) async throws {
        print("put restaurant \(restaurantID) to \(collectionID)")
        let request = try makeRequest(
            path: .collections(id: collectionID),
            method: .put,
            with: PutCollectionRequest(
                restaurantID: restaurantID
            )
        )
        
        try await performRequest(request: request)
        print("success")
    }
}
