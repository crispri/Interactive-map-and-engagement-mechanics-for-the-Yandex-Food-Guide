//
//  SmallPinView.swift
//  iosApp
//
//  Created by Христина Буянова on 16.08.2024.
//

import Foundation
import UIKit

final class SmallPinView: UIView {

    var isSelected = false

    private lazy var circle: UIImageView = {
        let image = UIImageView()
        image.frame = .init(x: 0, y: 0, width: 7, height: 7)
        image.image = UIImage(systemName: isSelected ? "circle.fill" : "circle")
        image.layer.shadowColor = UIColor.black.cgColor
        image.layer.shadowOffset = CGSize(width: 5, height: 5)
        image.layer.shadowOpacity = 0.5
        image.layer.shadowRadius = 5.0
        image.tintColor = .black
        return image
    }()

    //    MARK: init
        override init(frame: CGRect) {
            super.init(frame: frame)
            addSubview(circle)
        }

        required init?(coder: NSCoder) {
            fatalError("init(coder:) has not been implemented")
        }
}

#Preview {
    SmallPinView()
}
