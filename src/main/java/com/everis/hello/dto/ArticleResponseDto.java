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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleResponseDto implements Serializable{
	
	private static final long serialVersionUID = 6118800997897835994L;

	@JsonProperty("listName")
	private String listName;

	@JsonProperty("idArticleList")	
	private List<ProductDto> idArticleList;	

}
