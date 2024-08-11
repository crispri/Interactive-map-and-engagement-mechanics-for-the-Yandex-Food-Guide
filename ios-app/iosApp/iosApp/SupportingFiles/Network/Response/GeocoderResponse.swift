//
//  GeocoderResponse.swift
//  iosApp
//
//  Created by Stanislav Leonchik on 11.08.2024.
//

import Foundation

// MARK: - GeocoderResponse
struct GeocoderResponse: Codable {
    let response: GeoObjectCollectionResponse
}

// MARK: - GeoObjectCollectionResponse
struct GeoObjectCollectionResponse: Codable {
    let geoObjectCollection: GeoObjectCollection

    enum CodingKeys: String, CodingKey {
        case geoObjectCollection = "GeoObjectCollection"
    }
}

// MARK: - GeoObjectCollection
struct GeoObjectCollection: Codable {
    let metaDataProperty: MetaDataProperty
    let featureMember: [FeatureMember]
}

// MARK: - MetaDataProperty
struct MetaDataProperty: Codable {
    let geocoderResponseMetaData: GeocoderResponseMetaData

    enum CodingKeys: String, CodingKey {
        case geocoderResponseMetaData = "GeocoderResponseMetaData"
    }
}

// MARK: - GeocoderResponseMetaData
struct GeocoderResponseMetaData: Codable {
    let request: String
    let found: String
    let results: String
}

// MARK: - FeatureMember
struct FeatureMember: Codable {
    let geoObject: GeoObject

    enum CodingKeys: String, CodingKey {
        case geoObject = "GeoObject"
    }
}

// MARK: - GeoObject
struct GeoObject: Codable {
    let metaDataProperty: GeoObjectMetaDataProperty?
    let description: String?
    let name: String?
    let boundedBy: BoundedBy?
    let point: point?

    enum CodingKeys: String, CodingKey {
        case metaDataProperty
        case description
        case name
        case boundedBy = "boundedBy"
        case point = "Point"
    }
}

// MARK: - GeoObjectMetaDataProperty
struct GeoObjectMetaDataProperty: Codable {
    let geocoderMetaData: GeocoderMetaData?

    enum CodingKeys: String, CodingKey {
        case geocoderMetaData = "GeocoderMetaData"
    }
}

// MARK: - GeocoderMetaData
struct GeocoderMetaData: Codable {
    let kind: String?
    let text: String?
    let precision: String?
    let address: Address?
    let addressDetails: AddressDetails?

    enum CodingKeys: String, CodingKey {
        case kind
        case text
        case precision
        case address = "Address"
        case addressDetails = "AddressDetails"
    }
}

// MARK: - Address
struct Address: Codable {
    let countryCode: String?
    let postalCode: String?
    let formatted: String?
    let components: [AddressComponent?]?

    enum CodingKeys: String, CodingKey {
        case countryCode = "country_code"
        case postalCode = "postal_code"
        case formatted
        case components = "Components"
    }
}

// MARK: - AddressComponent
struct AddressComponent: Codable {
    let kind: String?
    let name: String?
}

// MARK: - AddressDetails
struct AddressDetails: Codable {
    let country: Country?

    enum CodingKeys: String, CodingKey {
        case country = "Country"
    }
}

// MARK: - Country
struct Country: Codable {
    let addressLine: String?
    let countryNameCode: String?
    let countryName: String?
    let administrativeArea: AdministrativeArea?

    enum CodingKeys: String, CodingKey {
        case addressLine = "AddressLine"
        case countryNameCode = "CountryNameCode"
        case countryName = "CountryName"
        case administrativeArea = "AdministrativeArea"
    }
}

// MARK: - AdministrativeArea
struct AdministrativeArea: Codable {
    let administrativeAreaName: String?
    let locality: Locality?

    enum CodingKeys: String, CodingKey {
        case administrativeAreaName = "AdministrativeAreaName"
        case locality = "Locality"
    }
}

// MARK: - Locality
struct Locality: Codable {
    let localityName: String?
    let thoroughfare: Thoroughfare?

    enum CodingKeys: String, CodingKey {
        case localityName = "LocalityName"
        case thoroughfare = "Thoroughfare"
    }
}

// MARK: - Thoroughfare
struct Thoroughfare: Codable {
    let thoroughfareName: String?
    let premise: Premise?

    enum CodingKeys: String, CodingKey {
        case thoroughfareName = "ThoroughfareName"
        case premise = "Premise"
    }
}

// MARK: - Premise
struct Premise: Codable {
    let premiseNumber: String?
    let postalCode: PostalCode?

    enum CodingKeys: String, CodingKey {
        case premiseNumber = "PremiseNumber"
        case postalCode = "PostalCode"
    }
}

// MARK: - PostalCode
struct PostalCode: Codable {
    let postalCodeNumber: String?

    enum CodingKeys: String, CodingKey {
        case postalCodeNumber = "PostalCodeNumber"
    }
}

// MARK: - BoundedBy
struct BoundedBy: Codable {
    let envelope: Envelope?

    enum CodingKeys: String, CodingKey {
        case envelope = "Envelope"
    }
}

// MARK: - Envelope
struct Envelope: Codable {
    let lowerCorner: String?
    let upperCorner: String?
}

// MARK: - Point
struct point: Codable {
    let pos: String
}
