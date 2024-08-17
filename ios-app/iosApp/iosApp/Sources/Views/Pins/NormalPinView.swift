//
//  NormalPinView.swift
//  iosApp
//
//  Created by Христина Буянова on 16.08.2024.
//

import Foundation
import UIKit

final class NormalPinView: UIView {

   private var isSelected = true
//    var isFavorit = true
    var model: SnippetDTO? {
        didSet {
//            imageRest.image = UIImage(named: model?. "1rest")
            nameRest.text = model?.name ?? "Название ресторана"
//            descriptionRest.text = model?.description ?? "Название ресторана"
            raiting.text = String(format: "%.1f" , model?.rating ?? "5.0")
            background()
        }
    }

    private lazy var squareView: UIView = {
        let view = UIView()
        view.frame = .init(x: 0, y: 9, width: 159, height: 30)
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
        triangle.frame = .init(x: 68, y: 39, width: 24, height: 8)
        return triangle
    }()

    private lazy var nameRest: UILabel = {
        let label = UILabel()
        label.frame = .init(x: 8, y: 8, width: 110, height: 14)
        label.font = .systemFont(ofSize: 13)
//        label.text = "Название ресторана"
        label.numberOfLines = 1
        return label
    }()

    private lazy var star: UIImageView = {
        let image = UIImageView()
        image.frame = .init(x: 121, y: 8, width: 11, height: 11)
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
        label.font = .systemFont(ofSize: 11)
//        label.text = "4.9"
        return label
    }()


//    MARK: init
    override init(frame: CGRect) {
        super.init(frame: frame)
        backgroundColor = .clear
        setupViews()
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
            raiting.textColor = .white
            favoriteView.backgroundColor = .black
            bookmark.tintColor = .white
        } else {
            squareView.backgroundColor = .white
            triangle.image = UIImage(named: "BottomCenter_white")
            star.tintColor = .black
            nameRest.textColor = .black
            raiting.textColor = .black
            favoriteView.backgroundColor = .white
            bookmark.tintColor = .black
        }
    }

    private func setupViews() {
        addSubview(squareView)
        addSubview(triangle)
        squareView.addSubview(nameRest)
        squareView.addSubview(star)
        squareView.addSubview(raiting)

        if model?.isFavorite ?? false {
            squareView.addSubview(favoriteView)
            favoriteView.addSubview(bookmark)
        }
    }

    func setSelected(_ isSelected: Bool) {
        self.isSelected = isSelected
        background()
    }
}

#Preview {
    NormalPinView()
}
