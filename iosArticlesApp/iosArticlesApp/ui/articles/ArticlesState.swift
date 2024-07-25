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
	
	let viewModel: ArticlesViewModel
	@Published var articles: DataOrException<NSArray, KotlinException, KotlinBoolean> = DataOrException(data: [], exception: nil, isLoading: true)
	
	
	init() {
		viewModel = ArticlesViewModel()
		viewModel.observeArticles { it in
			self.articles = it
		}
	}
		
	deinit {
		viewModel.clear()
	}

}
