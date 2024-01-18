//
//  ArticleView.swift
//  iosApp
//
//  Created by metro on 16/01/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

extension ArticleView {
    @MainActor
    class ArticleViewModelKs : ObservableObject {
        @Published
        var articleState: CommonState
        
        let articleViewModel: ArticleViewModel
        
        init() {
            self.articleViewModel = ArticleViewInjector().articleViewModel
            self.articleState = articleViewModel.articleState.value
        }
        
        func startObserveData() {
            Task{
                for await state in self.articleViewModel.articleState {
                    self.articleState = state
                }
            }
        }
    }
}

extension Article: Identifiable {
    
}

struct ArticleView: View {
    @ObservedObject
    private(set) var viewModel: ArticleViewModelKs
    
    var body: some View {
        NavigationView {
            ZStack(content: {
                switch onEnum(of: viewModel.articleState) {
                case .initial:
                    Text("initial")
                case .loading:
                    ProgressView()
                case .noData:
                    Text("no data")
                case .success(let articles):
                    if let articles: [Article] = articles.data as? [Article] {
                        List(articles) { article in
                            NavigationLink {
                                ArticleDetailView(viewModel: .init(), path: article.detailPath)
                            } label: {
                                ArticleItemView(article: article)
                            }
                        }
                    }
                    
                case .error(let error):
                    Text(error.message)
                    
                }
                
            }).navigationTitle("Article")
            .onAppear{
                viewModel.startObserveData()
                viewModel.articleViewModel.getArticles()
            }.onDisappear{
                viewModel.articleViewModel.destroy()
            }
        }
    }
}

struct ArticleItemView: View {
    let article: Article
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text(article.title).font(.title2).bold().multilineTextAlignment(.leading)
            
            AuthorView(author: article.author)
            
            HTMLText(htmlString: article.content)
        }
    }
}

#Preview {
    ArticleView(viewModel: .init())
}
