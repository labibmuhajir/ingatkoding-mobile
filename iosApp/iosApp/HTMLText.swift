//
//  HTMLText.swift
//  iosApp
//
//  Created by labibmuhajir on 17/01/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct HTMLText: View {
    let htmlString: String
    
    var body: some View {
        if let nsAttributedString = try? NSAttributedString(data: Data(htmlString.utf8), options: [.documentType: NSAttributedString.DocumentType.html], documentAttributes: nil),
           let attributedString = try? AttributedString(nsAttributedString, including: \.uiKit) {
            Text(attributedString).multilineTextAlignment(.leading)
        } else {
            Text(htmlString).multilineTextAlignment(.leading)
        }
    }
}

#Preview {
    HTMLText(htmlString: "")
}
