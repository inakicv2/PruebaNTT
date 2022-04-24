package com.everis.hello.dto;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ArticleRequestDto implements Serializable{
	
	private static final long serialVersionUID = 6118800997897835994L;
	@NotBlank
	@JsonProperty("listName")
	private String listName;

	@JsonProperty("idArticle")	
	private @Min(1)@Max(15)Integer idArticle;	

}
