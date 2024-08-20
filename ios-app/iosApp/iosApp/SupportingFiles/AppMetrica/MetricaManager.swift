//
//  MetricaManager.swift
//  iosApp
//
//  Created by Максим Кузнецов on 20.08.2024.
//

import Foundation
import AppMetricaCore

final class MetricaManager {
    static func logEvent(name: String, params: [AnyHashable : Any]?) {
        AppMetrica.reportEvent(name: name, parameters: params, onFailure: { (error) in
            print("DID FAIL REPORT EVENT: %@", name)
            print("REPORT ERROR: %@", error.localizedDescription)
        })
    }
}
