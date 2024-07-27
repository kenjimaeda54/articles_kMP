import SwiftUI
import shared


@main
struct iOSApp: App {
 
	init() {
		CommonModuleKt.doInitKoin()
	}

	var body: some Scene {
		WindowGroup {
			ArticlesScreen()
		}
	}
}


