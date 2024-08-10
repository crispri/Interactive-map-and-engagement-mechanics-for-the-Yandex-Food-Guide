//
//  Comparable+clamped.swift
//  iosApp
//
//  Created by Максим Кузнецов on 10.08.2024.
//

import Foundation

extension Comparable {
    
    /// Restrain value to range limits
    /// - Parameters:
    ///   - lower: lower limit
    ///   - upper: upper limit
    /// - Returns: Clammed value
    func clamped(_ lower: Self, _ upper: Self) -> Self {
        return min(max(self, lower), upper)
    }
}
