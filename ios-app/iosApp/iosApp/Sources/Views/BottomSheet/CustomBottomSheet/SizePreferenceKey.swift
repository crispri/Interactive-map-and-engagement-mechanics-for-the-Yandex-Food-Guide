//
//  SizePreferenceKey.swift
//  iosApp
//
//  Created by Максим Кузнецов on 10.08.2024.
//

import SwiftUI

/// Key for emerging size
@available(iOS 15.0, macOS 12.0, watchOS 10.0, *)
public struct SizePreferenceKey: PreferenceKey {
    
    public static var defaultValue = CGSize(width: 0, height: 0)
    
    public static func reduce(value: inout CGSize, nextValue: () -> CGSize) {
        value = nextValue()
    }
}
