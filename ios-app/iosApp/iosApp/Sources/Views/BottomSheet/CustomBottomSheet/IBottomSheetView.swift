//
//  IBottomSheetView.swift
//  iosApp
//
//  Created by Максим Кузнецов on 10.08.2024.
//

import SwiftUI

public protocol IBottomSheetView : View{
    
    associatedtype V : View
    
    // MARK: - Builder methods
    
    /// Hide dragging button
    func hideDragButton() -> Self
    
    /// Trun off animation
    func withoutAnimation() -> Self
    
    // MARK: - Event methods
    
    /// Handler for changing the sheet position
    func onPositionChanged(_ fn: @escaping (CustomBottomSheetPosition) -> ()) -> V
        
}
