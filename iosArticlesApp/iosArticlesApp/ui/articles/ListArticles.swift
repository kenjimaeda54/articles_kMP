//
//  ListArticles.swift
//  iosArticlesApp
//
//  Created by kenjimaeda on 25/07/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared
import URLImage


struct ListArticles: View {
	let articles: [ArticleModel]
	var body: some View {
		
		List(articles,id: \.self ) { article in
			VStack(spacing: 3){
				
				//precisar criar umma moldura identico a imagem que vai ser carregado da internet
				//se nao quando estiver carregando a imagem ira baguncar a UI
				URLImage(URL(string: article.imageUrl )!) {
					EmptyView()
				} inProgress: { progress in
					Image("notfound")
						.resizable()
						.frame(height: 230)
						.clipShape(RoundedRectangle(cornerRadius: 20))
				}failure: { error, retry in
					EmptyView()
				}  content: { image in
					image
						.resizable()
						.frame(height: 230)
						.clipShape(RoundedRectangle(cornerRadius: 20))
				}
				Text(article.title)
					.frame(maxWidth: .infinity,alignment: .leading)
					.lineLimit(/*@START_MENU_TOKEN@*/2/*@END_MENU_TOKEN@*/)
					.fontWeight(.bold)
					.font(.system(size: 20,design: .rounded))
				Text(article.description_)
					.frame(maxWidth: .infinity,alignment: .leading)
					.lineLimit(5)
					.fontWeight(.regular)
					.font(.system(size: 15,design: .rounded))
				Text(article.date)
					.frame(maxWidth: .infinity,alignment: .trailing)
					.fontWeight(.light)
					.font(.system(size: 12,design: .rounded))
				
				
			}
 			.listRowInsets(.none)
			.listRowBackground(Color.clear)
			.listRowSeparator(.hidden)
			
		}
		.listStyle(.plain)
		.scrollContentBackground(.hidden)
		.padding(EdgeInsets(top: 20, leading: 13, bottom: 20, trailing: 13))
		.scrollIndicators(.hidden)
			 

	
	}
}



