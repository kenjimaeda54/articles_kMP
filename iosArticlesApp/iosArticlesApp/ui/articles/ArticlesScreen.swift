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
			
			if(articleState.articles.exception != nil) {
				Text("Problema no servidor")
			}
			
			if let isLoading = articleState.articles.isLoading as? Bool, let data = articleState.articles.data as? [ArticleModel] {
				
				if(isLoading) {
					Text("Carregando")
				}
				
				
				
				if(data.isEmpty) {
					Text("Nao tem artigos")
				}else {
					ListArticles(articles: data)
				}
				
			}
		}
	}
}


//if let data = articleState.articles.data {
//if(data.count > 0){

//if let articles = data as? [Article] {
//Text(articles[ ])
//}

//}
//}
