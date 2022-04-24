package com.everis.hello.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.everis.hello.utils.UserUtils;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ArticleListDto implements Serializable{
	
	private static final long serialVersionUID = 6118800997897835994L;
	@NotBlank
	@JsonProperty("listName")
	private String listName;
	@NotEmpty
	@Size(min=UserUtils.MIN_ARTICLE_LIST, max=UserUtils.MAX_ARTICLE_LIST)
	@JsonProperty("idArticleList")	
	private List<@Min(1)@Max(15)Integer> idArticleList;	

}
