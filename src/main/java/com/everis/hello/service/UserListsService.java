package com.everis.hello.service;

import org.springframework.http.ResponseEntity;

import com.everis.hello.dto.ArticleListDto;

public interface UserListsService {
	
	ResponseEntity<?> createList(String username, ArticleListDto articleListDto); 
	ResponseEntity<?> addArticleToList(String username, String listname, Integer articleId);	
	ResponseEntity<?> deleteList(String username, String listname);
	ResponseEntity<?> deleteArticleFromList(String username, String listname, Integer articleId);	
	ResponseEntity<?> getListDetailByListname(String username, String listname);
	ResponseEntity<?> getAllListsFromUser(String username);
	
	
}
