package com.everis.hello.utils;

import java.util.List;

import org.springframework.security.oauth2.jwt.Jwt;

import com.everis.hello.model.ArticleList;

public class UserUtils {
	
	public static final int MAX_USER_LIST = 5;
	
	public static final int MIN_USER_LIST = 1;
	
	public static final int MIN_ARTICLE_LIST = 1;
	
	public static final int MAX_ARTICLE_LIST = 25;
	
	public static final String USERNAME = "preferred_username";
	
	public static String getJwtUsername(Jwt principal) {
		return principal.getClaimAsStringList(USERNAME).stream().findFirst().orElseThrow(RuntimeException::new);
	}
	
	public static boolean articleListContainsName(List<ArticleList> list, String name) {
		 return list.stream().anyMatch(obj -> obj.getListName().equals(name));
	}

}
