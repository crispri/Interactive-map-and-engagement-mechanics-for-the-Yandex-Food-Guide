import SwiftUI
import YandexMapsMobile
import AppMetricaCore

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        YMKMapKit.setApiKey("d67cbfc8-2364-4890-a8c7-fe1268a02555")
        YMKMapKit.sharedInstance()
        
        let configuration = AppMetricaConfiguration(apiKey: "3aa75b72-f41e-4ca3-92c9-e578fbbd22de")
        AppMetrica.activate(with: configuration!)
        return true
    }
}

@main
struct iosApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    
    var body: some Scene {
        WindowGroup {
            GuideView()
        }
    }
}
