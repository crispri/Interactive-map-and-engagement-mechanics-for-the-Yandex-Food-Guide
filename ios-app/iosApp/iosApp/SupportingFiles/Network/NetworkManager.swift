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
    
    func makeRequest(path: String, method: String, requestModel: Codable) throws -> URLRequest {
        guard let url = URL(string: path, relativeTo: baseURL) else {
            throw URLError(.badURL)
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = method
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.setValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
        
        let encoder = JSONEncoder()
        encoder.outputFormatting = .prettyPrinted
        let jsonData = try encoder.encode(requestModel)
        request.httpBody = jsonData
          
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
    
    public func fetchSnippets(lowerLeftCorner: Point, topRightCorner: Point) async throws -> [SnippetDTO] {
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
}
