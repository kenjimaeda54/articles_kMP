//
//  ArticlesState.swift
//  iosArticlesApp
//
//  Created by kenjimaeda on 25/07/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import shared

class ArticlesState: ObservableObject {
	
	@Published var loading = LoadingState.loading
	let viewModel: ArticlesViewModel = ArticlesViewModel()
	var articlesModel: [ArticleModel] = []
	
	
	//trabalhar com skie mais flow
	//https://skie.touchlab.co/features/flows	@MainActor
	func fetchArticles() async  {
    
		for await viewModel in viewModel.articles {
			
			if(viewModel.exception != nil){
				loading = .failure
			}
			
			if let data = viewModel.data as? [ArticleModel] {
				self.loading = .sucess
				self.articlesModel = data
			}
			
			
 		}
		
	}
	
	
	
	
	deinit {
		viewModel.clear()
	}
	
}
