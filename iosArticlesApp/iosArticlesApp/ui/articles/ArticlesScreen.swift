//
//  ArticlesScreen.swift
//  iosArticlesApp
//
//  Created by kenjimaeda on 25/07/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared


struct ArticlesScreen: View {
	@ObservedObject var articleState = ArticlesState()
	
	var body: some View {
		
		VStack {
			Text("Articles")
				.font(.system(size: 20,design: .rounded))
				.fontWeight(.bold)
			
			
			switch articleState.loading {
				case .failure:
					Text("Problema no servidor")
					
				case .loading:
					Text("Carregando")
					
				case .sucess:
					if(articleState.articlesModel.isEmpty) {
						Text("Nao tem artigos")
						
					}else {
						ListArticles(articles: articleState.articlesModel)
							.refreshable {
								articleState.refreshDataArticles()
							}
					}
					
			}
			
		}
		.task {
			await articleState.fetchArticles()
		}
	}
	
}


