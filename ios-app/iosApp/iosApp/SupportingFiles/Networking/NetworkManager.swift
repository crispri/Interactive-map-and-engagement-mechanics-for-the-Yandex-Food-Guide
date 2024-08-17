//
//  NetworkManager.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 04.08.2024.
//

import Foundation

final class NetworkManager {
    private let baseURL = URL(string: Api.baseURL)
    private let token = Api.token
    
    private func makeRequest(path: String, method: String) throws -> URLRequest {
        guard let url = URL(string: path, relativeTo: baseURL) else {
            throw URLError(.badURL)
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = method
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.setValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
          
        return request
    }
    
    private func makeRequest(path: String, method: String, with requestModel: Encodable?) throws -> URLRequest {
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
    
    func fetchSnippets(lowerLeftCorner: Point, topRightCorner: Point, filters: [FilterDTO]? = nil) async throws -> [SnippetDTO] {
        let request = try makeRequest(
            path: Api.restaurants.path,
            method: HTTPMethod.post.rawValue,
            with: FilteredSnippetRequest(
                lowerLeftCorner: lowerLeftCorner,
                topRightCorner: topRightCorner,
                maxCount: 0,
                filters: filters
            )
        )
        
        let data: SnippetsResponse = try await performRequest(request: request)
        
        return data.items
    }
    
    func fetchSnippet(id: String) async throws -> SnippetDTO {
        let request = try makeRequest(
            path: Api.restaurant(id: id).path,
            method: HTTPMethod.get.rawValue
        )
        
        let data: SnippetDTO = try await performRequest(request: request)
        
        return data
    }
    
    func fetchSelections() async throws -> [SelectionDTO] {
        let request = try makeRequest(
            path: Api.selections.path,
            method: HTTPMethod.get.rawValue
        )
        
        let data: SelectionsResponse = try await performRequest(request: request)
        
        return data.items
    }

    func fetchSelectionSnippets(id: String) async throws -> [SnippetDTO] {
        let request = try makeRequest(
            path: Api.selection(id: id).path,
            method: HTTPMethod.get.rawValue
        )
        
        let data: SnippetsResponse = try await performRequest(request: request)
        
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
}
