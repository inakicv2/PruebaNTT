package com.everis.hello.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everis.hello.dto.ArticleListDto;
import com.everis.hello.dto.ArticleRequestDto;
import com.everis.hello.service.UserListsService;
import com.everis.hello.utils.UserUtils;


@Validated
@RestController
@RequestMapping("/user-lists")
public class UserListsController {
	
	@Autowired
    private UserListsService userListsService;

	@PostMapping(value = "/create-list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createList(@AuthenticationPrincipal Jwt principal, @RequestBody @Valid ArticleListDto articleListDto) {
		return userListsService.createList(UserUtils.getJwtUsername(principal), articleListDto);       
    }	
	
	@PutMapping(value = "/add-article-to-list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addArticleToList(@AuthenticationPrincipal Jwt principal, @RequestBody @Valid ArticleRequestDto articleRequetsDto) {
		return userListsService.addArticleToList(UserUtils.getJwtUsername(principal), articleRequetsDto.getListName(), articleRequetsDto.getIdArticle());       
    }
	
	@DeleteMapping(value = "/delete-list/{listname}")
    public ResponseEntity<?> deleteList(@AuthenticationPrincipal Jwt principal, @PathVariable("listname") String listname) {
		return userListsService.deleteList(UserUtils.getJwtUsername(principal), listname);       
    }
	
	@DeleteMapping(value = "/delete-article-from-list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteArticleFromList(@AuthenticationPrincipal Jwt principal, @RequestBody @Valid ArticleRequestDto articleRequetsDto) {
		return userListsService.deleteArticleFromList(UserUtils.getJwtUsername(principal), articleRequetsDto.getListName(), articleRequetsDto.getIdArticle());       
    }
	
	@GetMapping(value = "/get-list-detail-by-listname/{listname}")
    public ResponseEntity<?> getListDetailByListname(@AuthenticationPrincipal Jwt principal,  @PathVariable("listname") String listname) {
		return userListsService.getListDetailByListname(UserUtils.getJwtUsername(principal), listname);       
    }
	
	@GetMapping(value = "/get-all-lists-from-user")
    public ResponseEntity<?> getAllListsFromUser(@AuthenticationPrincipal Jwt principal) {
		return userListsService.getAllListsFromUser(UserUtils.getJwtUsername(principal));       
    }

}
