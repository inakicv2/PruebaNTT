package com.everis.hello.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Document("user")
@AllArgsConstructor
public class User {
	@Id
	private String username;
	private List<ArticleList> articleList;

}
