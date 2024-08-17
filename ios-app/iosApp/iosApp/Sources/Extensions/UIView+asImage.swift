//
//  UIView+asImage.swift
//  iosApp
//
//  Created by Максим Кузнецов on 17.08.2024.
//

import UIKit

extension UIView {
    func asImage() -> UIImage {
        let renderer = UIGraphicsImageRenderer(bounds: bounds)
        return renderer.image { rendererContext in
            layer.render(in: rendererContext.cgContext)
        }
    }
}
