//
//  AuthorView.swift
//  iosApp
//
//  Created by metro on 18/01/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct AuthorView: View {
    let author: Author
    
    var body: some View {
        HStack(alignment: .center) {
            AsyncImage(url: URL(string: author.imageUrl)) { phase in
                if phase.image != nil {
                    phase.image!.resizable()
                        .aspectRatio(contentMode: .fit)
                } else if phase.error != nil {
                    Text("Image Error")
                } else {
                    ProgressView()
                }
            }.frame(width: 32, height: 32).clipShape(Circle())
            
            
            
            Text(author.name).font(.caption).foregroundStyle(.gray)
        }.padding(.bottom, 8)
    }
}
