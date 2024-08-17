//
//  SmallPinView.swift
//  iosApp
//
//  Created by Христина Буянова on 16.08.2024.
//

import Foundation
import UIKit

final class SmallPinView: UIView {

   private var isSelected = false

    private lazy var circleView: UIView = {
        let view = UIView()
        view.frame = .init(x: 0, y: 0, width: 7, height: 7)
        view.layer.cornerRadius = 12
        view.layer.shadowColor = UIColor.black.cgColor
        view.layer.shadowOffset = CGSize(width: 0, height: 3)
        view.layer.shadowOpacity = 0.5
        view.layer.shadowRadius = 5.0
        view.tintColor = .black
        return view
    }()

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
            backgroundColor = .clear
            addSubview(circleView)
            circleView.addSubview(circle)
        }

        required init?(coder: NSCoder) {
            fatalError("init(coder:) has not been implemented")
        }

    func setSelected(_ isSelected: Bool) {
        self.isSelected = isSelected
        circle.image = UIImage(systemName: isSelected ? "circle.fill" : "circle")
    }
}

#Preview {
    SmallPinView()
}
