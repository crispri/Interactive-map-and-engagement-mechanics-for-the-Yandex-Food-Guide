//
//  NetworkManager.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 04.08.2024.
//

import Foundation

final class NetworkManager {
    private let baseURL = URL(string: Configuration.baseURL)
    private let token = Configuration.token
    
    func makeRequest(path: String, method: String, requestModel: Codable?) throws -> URLRequest {
        guard let url = URL(string: path, relativeTo: baseURL) else {
            throw URLError(.badURL)
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = method
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.setValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
        
        if let requestModel {
            let encoder = JSONEncoder()
            encoder.outputFormatting = .prettyPrinted
            let jsonData = try encoder.encode(requestModel)
            request.httpBody = jsonData
        }
          
        return request
    }
    
    func performRequest<T: Decodable>(request: URLRequest) async throws -> T {
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
    
    func fetchSnippets(lowerLeftCorner: Point, topRightCorner: Point) async throws -> [SnippetDTO] {
        let request = try makeRequest(
            path: "restaurants",
            method: "POST",
            requestModel: GetSnippetsRequestModel(
                lowerLeftCorner: lowerLeftCorner,
                topRightCorner: topRightCorner,
                maxCount: 0
            )
        )
        
        let data: SnippetsResponse = try await performRequest(request: request)
        
        return data.items
    }
    
    func fetchSelections() async throws -> [SelectionDTO] {
        let request = try makeRequest(
            path: "selections",
            method: "GET",
            requestModel: nil
        )
        
        let data: SelectionsResponse = try await performRequest(request: request)
        
        return data.items
    }
    
    func fetchSelectionSnippets(id: String) async throws -> [SnippetDTO] {
        let request = try makeRequest(
            path: "selections/\(id)",
            method: "GET",
            requestModel: nil
        )
        
        let data: SnippetsResponse = try await performRequest(request: request)
        
        return data.items
    }

    public func fetchAddress(loc: Point) async throws -> Data {
        var components = URLComponents(string: "\(Configuration.geocoderURL)")!

        components.queryItems = [
            URLQueryItem(name: "apikey", value: "\(Configuration.geocoderKey)"),
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
}
