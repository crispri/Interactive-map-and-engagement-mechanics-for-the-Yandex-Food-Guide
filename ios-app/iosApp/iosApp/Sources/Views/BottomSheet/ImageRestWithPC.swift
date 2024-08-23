
import SwiftUI

struct ImageRestWithPC<Page: View>: View {
    var pages: [Page]
    @State private var currentPage = 0

    var body: some View {
        ZStack(alignment: .bottomTrailing) {
            PageViewController(pages: pages, currentPage: $currentPage)
            PageControl(numberOfPages: pages.count, currentPage: $currentPage)
                .frame(width: CGFloat(pages.count * 18))
                .padding(.trailing)
        }
        .cornerRadius(24)
    }
}

let images = [Image("1rest"), Image("2rest"), Image("3rest")].map { $0.resizable() }
#Preview {
    ImageRestWithPC(pages: images)
        .frame(height: 150)
}
