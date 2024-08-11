//
//  BigPinView.swift
//  iosApp
//
//  Created by Христина Буянова on 11.08.2024.
//

import Foundation
import UIKit

final class BigPinView: UIView {

    var model: SnippetDTO? {
        didSet {
//            imageRest.image = UIImage(named: model?. "1rest")
            nameRest.text = model?.name ?? "Название ресторана"
//            descriptionRest.text = model?.description ?? "Название ресторана"
            raiting.text = String(format: "%.1f" , model?.rating ?? "5.0")
            isFavorit = model?.isFavorite ?? false
        }
    }

    var isSelected = false
    var isFavorit = false

    private lazy var squareView: UIView = {
        let view = UIView()
        view.layer.cornerRadius = 12
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()

    private lazy var favoriteView: UIView = {
        let view = UIView()
        view.layer.cornerRadius = 12
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()

    private lazy var triangle: UIImageView = {
        let triangle = UIImageView()
        triangle.translatesAutoresizingMaskIntoConstraints = false
        return triangle
    }()

    private lazy var imageRest: UIImageView = {
        let image = UIImageView()
        image.image = UIImage(named: "1rest")
        image.layer.cornerRadius = 12
        image.layer.maskedCorners = [.layerMaxXMinYCorner, .layerMinXMinYCorner]
        image.clipsToBounds = true
        image.translatesAutoresizingMaskIntoConstraints = false
        return image
    }()

    private lazy var nameRest: UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 13)
        label.text = "Название ресторана"
        label.translatesAutoresizingMaskIntoConstraints = false
        label.numberOfLines = 1
        return label
    }()

    private lazy var descriptionRest: UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 11)
        label.numberOfLines = 1
        label.text = "кофе от 300р"
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()

    private lazy var star: UIImageView = {
        let image = UIImageView()
        image.image = UIImage(systemName: "star.fill")
        image.translatesAutoresizingMaskIntoConstraints = false
        return image
    }()

    private lazy var bookmark: UIImageView = {
        let image = UIImageView()
        image.image = UIImage(systemName: "bookmark.fill")
        image.translatesAutoresizingMaskIntoConstraints = false
        return image
    }()


    private lazy var raiting: UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 11)
        label.text = "4.9"
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()


//    MARK: init
    override init(frame: CGRect) {
        super.init(frame: frame)
//        backgroundColor = .gray
        setupViews()
        background()
        setupLayout()
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

        if ((model?.isFavorite) != nil) {
            addSubview(favoriteView)
            favoriteView.addSubview(bookmark)
        }
    }

    private func setupLayout() {
        NSLayoutConstraint.activate([
            imageRest.centerXAnchor.constraint(equalTo: centerXAnchor),
            imageRest.centerYAnchor.constraint(equalTo: centerYAnchor),
            imageRest.widthAnchor.constraint(equalToConstant: 160),
            imageRest.heightAnchor.constraint(equalToConstant: 60),

            squareView.centerXAnchor.constraint(equalTo: imageRest.centerXAnchor),
            squareView.topAnchor.constraint(equalTo: imageRest.topAnchor, constant: 50),
            squareView.widthAnchor.constraint(equalToConstant: 160),
            squareView.heightAnchor.constraint(equalToConstant: 43),

            triangle.topAnchor.constraint(equalTo: squareView.bottomAnchor),
            triangle.centerXAnchor.constraint(equalTo: squareView.centerXAnchor),

            nameRest.topAnchor.constraint(equalTo: squareView.topAnchor, constant: 8),
            nameRest.leadingAnchor.constraint(equalTo: squareView.leadingAnchor, constant: 8),
            nameRest.widthAnchor.constraint(equalToConstant: 113),

            descriptionRest.bottomAnchor.constraint(equalTo: squareView.bottomAnchor, constant: -8),
            descriptionRest.leadingAnchor.constraint(equalTo: squareView.leadingAnchor, constant: 8),

            star.topAnchor.constraint(equalTo: squareView.topAnchor, constant: 8),
            star.widthAnchor.constraint(equalToConstant: 12),
            star.heightAnchor.constraint(equalToConstant: 12),
            star.leadingAnchor.constraint(equalTo: nameRest.trailingAnchor),

            raiting.leadingAnchor.constraint(equalTo: star.trailingAnchor),
            raiting.topAnchor.constraint(equalTo: squareView.topAnchor, constant: 8),

            favoriteView.widthAnchor.constraint(equalToConstant: 18),
            favoriteView.heightAnchor.constraint(equalToConstant: 18),
            favoriteView.topAnchor.constraint(equalTo: squareView.topAnchor, constant: -9),
            favoriteView.trailingAnchor.constraint(equalTo: squareView.trailingAnchor, constant: -9),

            bookmark.widthAnchor.constraint(equalToConstant: 12),
            bookmark.heightAnchor.constraint(equalToConstant: 12),
            bookmark.centerXAnchor.constraint(equalTo: favoriteView.centerXAnchor),
            bookmark.centerYAnchor.constraint(equalTo: favoriteView.centerYAnchor)
        ])
    }
}


#Preview {
    BigPinView()
}
