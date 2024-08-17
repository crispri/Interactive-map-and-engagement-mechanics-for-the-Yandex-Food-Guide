//
//  SmallPinView.swift
//  iosApp
//
//  Created by Христина Буянова on 16.08.2024.
//

import Foundation
import UIKit

final class SmallPinView: UIView {

    var model: SnippetDTO

    private lazy var circleView: UIView = {
        let view = UIView()
        view.frame = .init(x: 0, y: 0, width: 11, height: 11)
        view.backgroundColor = .white
        view.layer.cornerRadius = 5
        view.layer.shadowColor = UIColor.black.cgColor
        view.layer.shadowOffset = CGSize(width: 0, height: 3)
        view.layer.shadowOpacity = 0.5
        view.layer.shadowRadius = 5.0
        return view
    }()

    private lazy var borderCircle: UIImageView = {
        let image = UIImageView()
        image.frame = .init(x: 1, y: 1, width: 9, height: 9)
        image.image = UIImage(systemName: "circle.fill")
        image.tintColor = .black
        return image
    }()

    private lazy var whiteCircle: UIImageView = {
        let image = UIImageView()
        image.frame = .init(x: 1, y: 1.15, width: 7, height: 7)
        image.image = UIImage(systemName: "circle.fill")
        image.tintColor = .white
        return image
    }()

    private lazy var ultimaImage: UIImageView = {
        let image = UIImageView()
        image.frame = .init(x: 0, y: 0, width: 11, height: 11)
        image.image = UIImage(named: "ultimaIcon")
        return image
    }()

    private lazy var redactionImage: UIImageView = {
        let image = UIImageView()
        image.frame = .init(x: 0, y: 0, width: 11, height: 11)
        image.image = UIImage(named: "redactionIcon")
        return image
    }()

    //    MARK: init
     init(frame: CGRect, model: SnippetDTO) {
        self.model = model
        super.init(frame: frame)
            backgroundColor = .clear

            if model.tags.contains("ULTIMA GUIDE") {
                addSubview(ultimaImage)
            } else if model.tags.contains("Открытая кухня") {
                addSubview(redactionImage)
            } else {
                addSubview(circleView)
                circleView.addSubview(borderCircle)
                borderCircle.addSubview(whiteCircle)
            }
        }

        required init?(coder: NSCoder) {
            fatalError("init(coder:) has not been implemented")
        }
}

#Preview {
    SmallPinView(frame: .init(x: 0, y: 0, width: 11, height: 11), model: SnippetDTO.mockData[0])
}
