//
//  ErrorView.swift
//  iosApp
//
//  Created by labibmuhajir on 22/01/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ErrorView: View {
    let message: String
    let retry: () -> Void
    
    var body: some View {
        VStack(
            alignment: HorizontalAlignment.center,
            spacing: 8
        ) {
            Text(message)
            
            Button {
                retry()
            } label: {
                Text("Retry")
            }

        }
    }
}

#Preview {
    ErrorView(message: "Error") {
        
    }
}
