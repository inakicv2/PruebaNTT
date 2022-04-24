package com.everis.hello.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArticleList {
	@Indexed(unique = true)
	@Id
	private String listName;
	private List<Integer> idArticleList;	

}
