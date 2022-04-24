package com.everis.hello.utils;

import org.mapstruct.Mapper;

import com.everis.hello.dto.ArticleListDto;
import com.everis.hello.model.ArticleList;

@Mapper(componentModel = "spring")
public interface ArticleListsMapper {
    
    ArticleListDto articleListToArticleListDto(ArticleList articleList);
    
    ArticleList articleListDtoToArticleList(ArticleListDto articleListDto);

}
