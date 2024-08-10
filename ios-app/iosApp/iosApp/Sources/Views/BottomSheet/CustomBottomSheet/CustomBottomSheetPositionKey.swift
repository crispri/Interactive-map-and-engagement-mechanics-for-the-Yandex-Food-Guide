//
//  CustomBottomSheetPositionKey.swift
//  iosApp
//
//  Created by Максим Кузнецов on 10.08.2024.
//

import SwiftUI

/// Key for emerging drawer sheet position
@available(iOS 15.0, macOS 12.0, watchOS 10.0, *)
public struct CustomBottomSheetPositionKey: PreferenceKey {
    
    static public var defaultValue: CustomBottomSheetPosition = .down(0)

    static public func reduce(value: inout CustomBottomSheetPosition, nextValue: () -> CustomBottomSheetPosition) {
        value = nextValue()
    }
}
