//
//  ArticleDetailView.swift
//  iosApp
//
//  Created by labibmuhajir on 17/01/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

extension ArticleDetailView {
    @MainActor
    class ArticleDetailViewModelKs: ObservableObject {
        @Published
        var articleState: CommonState
        
        let articleDetailViewModel: ArticleDetailViewModel
        
        init() {
            self.articleDetailViewModel = ArticleDetailViewInjector().articleDetailViewModel
            self.articleState = articleDetailViewModel.articleState.value
        }
        
        func startObserveData() {
            Task {
                for await state in self.articleDetailViewModel.articleState {
                    self.articleState = state
                }
            }
        }
        
    }
}

struct ArticleDetailView: View {
    @ObservedObject
    private(set) var viewModel: ArticleDetailViewModelKs
    
    let path: String
    
    var body: some View {
        ZStack(content: {
            switch onEnum(of: viewModel.articleState) {
            case .initial:
                VStack{}
            case .loading:
                ProgressView()
            case .noData:
                Text("No Data")
            case .error(let error):
                ErrorView(message: error.message, retry: error.retry)
            case .success(let state):
                if let article = state.data as? ArticleDetail {
                    ScrollView {
                        VStack(alignment: HorizontalAlignment.leading, spacing: 8) {
                            Text(article.title)
                            
                            AuthorView(author: article.author)
                            
                            HTMLText(htmlString: article.content)
                        }.padding([.horizontal], 16)
                    }
                }
            }
        }).onAppear{
            viewModel.startObserveData()
            viewModel.articleDetailViewModel.getArticle(path: path)
        }.onDisappear{
            viewModel.articleDetailViewModel.destroy()
        }
    }
}

#Preview {
    ArticleDetailView(viewModel: .init(), path: "")
}
