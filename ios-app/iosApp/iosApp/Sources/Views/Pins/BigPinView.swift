//
//  BigPinView.swift
//  iosApp
//
//  Created by Христина Буянова on 11.08.2024.
//

import Foundation
import UIKit

final class BigPinView: UIView {

   private var isSelected = true
    var model: SnippetDTO

    private lazy var squareView: UIView = {
        let view = UIView()
        view.frame = .init(x: 0, y: 50, width: 160, height: 43)
        view.layer.cornerRadius = 12
        view.layer.shadowColor = UIColor.black.cgColor
        view.layer.shadowOffset = CGSize(width: 0, height: 5)
        view.layer.shadowOpacity = 0.5
        view.layer.shadowRadius = 5.0
        view.tintColor = .black
        return view
    }()

    private lazy var favoriteView: UIView = {
        let view = UIView()
        view.frame = .init(x: 148, y: -9, width: 18, height: 18)
        view.layer.cornerRadius = 6
        return view
    }()

    private lazy var triangle: UIImageView = {
        let triangle = UIImageView()
        triangle.frame = .init(x: 68, y: 93, width: 24, height: 8)
        return triangle
    }()

    private lazy var imageRest: UIImageView = {
        let image = UIImageView()
        image.frame = .init(x: 0, y: 0, width: 160, height: 60)
        image.clipsToBounds = true
        image.image = UIImage(named: "1rest")
        image.layer.cornerRadius = 12
        image.layer.maskedCorners = [.layerMaxXMinYCorner, .layerMinXMinYCorner]
        return image
    }()

    private lazy var nameRest: UILabel = {
        let label = UILabel()
        label.frame = .init(x: 8, y: 8, width: 113, height: 14)
        label.font = UIFont(name: "YS-Bold", size: 13)
//        label.font = .systemFont(ofSize: 13)
        label.text = model.name
        label.numberOfLines = 1
        return label
    }()

    private lazy var descriptionRest: UILabel = {
        let label = UILabel()
        label.frame = .init(x: 8, y: 23, width: 144, height: 12)
        label.font = UIFont(name: "YS-Regular", size: 11)
//        label.font = .systemFont(ofSize: 11)
        label.numberOfLines = 1
        label.text = "\(model.additionalInfo)"
        return label
    }()

    private lazy var star: UIImageView = {
        let image = UIImageView()
        image.frame = .init(x: 121, y: 8, width: 11, height: 12)
        image.image = UIImage(systemName: "star.fill")
        return image
    }()

    private lazy var bookmark: UIImageView = {
        let image = UIImageView()
        image.frame = .init(x: 5, y: 5, width: 8, height: 8)
        image.image = UIImage(systemName: "bookmark.fill")
        return image
    }()


    private lazy var raiting: UILabel = {
        let label = UILabel()
        label.frame = .init(x: 132, y: 8, width: 20, height: 12)
        label.font = UIFont(name: "YS-Regular", size: 11)
//        label.font = .systemFont(ofSize: 11)
        label.text = String(format: "%.1f" , model.rating)
        return label
    }()

    private lazy var ultima: UIImageView = {
        let image = UIImageView()
        image.image = UIImage(named: "ultima")
        return image
    }()

    private lazy var redaction: UIImageView = {
        let image = UIImageView()
        image.image = UIImage(named: "redaction")
        return image
    }()

//    MARK: init
    init(frame: CGRect, model: SnippetDTO) {
        self.model = model
        super.init(frame: frame)
        backgroundColor = .clear
        setupViews()
        background()
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

//    MARK: func
    private func background() {
        if isSelected {
            squareView.backgroundColor = .black
            triangle.image = UIImage(named: "BottomCenter_black")
            star.tintColor = .white
            nameRest.textColor = .white
            descriptionRest.textColor = .white
            raiting.textColor = .white
            favoriteView.backgroundColor = .black
            bookmark.tintColor = .white
        } else {
            squareView.backgroundColor = .white
            triangle.image = UIImage(named: "BottomCenter_white")
            star.tintColor = .black
            nameRest.textColor = .black
            descriptionRest.textColor = .black
            raiting.textColor = .black
            favoriteView.backgroundColor = .white
            bookmark.tintColor = .black
        }
    }

    private func setupViews() {
        addSubview(imageRest)
        addSubview(squareView)
        addSubview(triangle)
        squareView.addSubview(nameRest)
        squareView.addSubview(descriptionRest)
        squareView.addSubview(star)
        squareView.addSubview(raiting)

        if model.inCollection {
            squareView.addSubview(favoriteView)
            favoriteView.addSubview(bookmark)
        }

        if model.tags.contains("ULTIMA GUIDE") && model.tags.contains("Открытая кухня") {
            ultima.frame = .init(x: 4, y: 4, width: 16, height: 16)
            redaction.frame = .init(x: 24, y: 4, width: 16, height: 16)
            imageRest.addSubview(ultima)
            imageRest.addSubview(redaction)
        } else if model.tags.contains("ULTIMA GUIDE") {
            ultima.frame = .init(x: 4, y: 4, width: 16, height: 16)
            imageRest.addSubview(ultima)
        } else if model.tags.contains("Открытая кухня") {
            redaction.frame = .init(x: 4, y: 4, width: 16, height: 16)
            imageRest.addSubview(redaction)
        }
    }

    func setSelected(_ isSelected: Bool) {
        self.isSelected = isSelected
        background()
    }
}


#Preview {
    BigPinView(frame: .init(x: 0, y: 0, width: 200, height: 200), model: SnippetDTO.mockData[0])
}
