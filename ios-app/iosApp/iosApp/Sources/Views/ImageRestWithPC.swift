
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
        .frame(height: 150)
        .aspectRatio(contentMode: .fill)
        .cornerRadius(24)
    }
}


#Preview {
    ImageRestWithPC(pages: [Image("1rest"), Image("2rest"), Image("3rest")])
}
