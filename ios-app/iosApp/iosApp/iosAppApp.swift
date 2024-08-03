import SwiftUI
import YandexMapsMobile

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        YMKMapKit.setApiKey("d67cbfc8-2364-4890-a8c7-fe1268a02555")
        YMKMapKit.sharedInstance()
        return true
    }
}

@main
struct iosAppApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
