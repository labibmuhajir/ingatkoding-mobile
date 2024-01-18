import SwiftUI
import shared

@main
struct iOSApp: App {
    init() {
        InjectorKt.startKoin()
    }
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
