//
//  ArticleView.swift
//  iosApp
//
//  Created by labibmuhajir on 16/01/24.
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
                    VStack{}
                case .loading:
                    ProgressView()
                case .noData:
                    Text("no data")
                case .success(let articleState):
                    if let paging: Paging = articleState.data as? Paging<Article>, let articles: [Article] = paging.currentData as? [Article]  {
                        
                            
                        SuccessContent(articles: articles, nextState: paging.nextState) {
                            viewModel.articleViewModel.getNextArticle()
                        }
                    }
                case .error(let error):
                    ErrorView(message: error.message, retry: error.retry)
                }
                
            }).navigationTitle("Article").navigationBarTitleDisplayMode(.inline)
            .onAppear{
                viewModel.startObserveData()
                viewModel.articleViewModel.getArticles()
            }.onDisappear{
                viewModel.articleViewModel.destroy()
            }
        }
    }
}

struct SuccessContent: View {
    let articles: [Article]
    let nextState: NextState
    let onLoadNext: () -> Void
    
    var body: some View {
        ScrollView {
            LazyVStack {
                ForEach(articles) { article in
                    NavigationLink {
                        ArticleDetailView(viewModel: .init(), path: article.detailPath)
                    } label: {
                        ArticleItemView(article: article)
                    }
                }
                
                switch onEnum(of: nextState) {
                case .loading:
                    VStack(alignment: HorizontalAlignment.center) {
                        ProgressView()
                    }.onAppear(perform: onLoadNext)
                    
                case .error(let error):
                    ErrorView(message: error.message, retry: error.retry)
                case .endPage:
                    Spacer()
                }
            }.padding([.horizontal], 16).id(1)
        }
    }
}

struct ArticleItemView: View {
    let article: Article
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text(article.title).font(.title2).bold().multilineTextAlignment(.leading).foregroundStyle(.black)
            
            AuthorView(author: article.author)
            
            HTMLText(htmlString: article.content)
        }
    }
}

#Preview {
    ArticleView(viewModel: .init())
}
