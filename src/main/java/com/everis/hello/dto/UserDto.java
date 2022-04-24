package com.everis.hello.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.everis.hello.utils.UserUtils;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDto implements Serializable{

	private static final long serialVersionUID = -6793971461424668770L;
	@NotBlank
	@JsonProperty("username")
	private String username;
	@NotEmpty
	@Size(min=UserUtils.MIN_USER_LIST, max=UserUtils.MAX_USER_LIST)
	@JsonProperty("articleList")
	@Valid private List<ArticleListDto> articleList;

}
