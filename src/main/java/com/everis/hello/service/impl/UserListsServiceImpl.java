package com.everis.hello.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.everis.hello.dto.ArticleListDto;
import com.everis.hello.dto.ArticleResponseDto;
import com.everis.hello.dto.ProductDto;
import com.everis.hello.dto.UserDto;
import com.everis.hello.model.ArticleList;
import com.everis.hello.model.User;
import com.everis.hello.repository.UserRepository;
import com.everis.hello.service.UserListsService;
import com.everis.hello.utils.ArticleListsMapper;
import com.everis.hello.utils.UserMapper;
import com.everis.hello.utils.UserUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Service
public class UserListsServiceImpl implements UserListsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ArticleListsMapper articleListsMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private Validator validator;
	@Autowired
	ObjectMapper objectMapper;
	
	WebClient client = WebClient.builder()
			  .baseUrl("http://localhost:8081")
			  .defaultCookie("cookieKey", "cookieValue")
			  .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) 
			  .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8081"))
			  .build();

	@Override
	public ResponseEntity<?> createList(String username, ArticleListDto articleListDto) {
		Optional<User> user = this.userRepository.findById(username);
		ArticleList articleListToAdd = articleListsMapper.articleListDtoToArticleList(articleListDto);
		user.ifPresentOrElse(response -> {
			if (!UserUtils.articleListContainsName(response.getArticleList(), articleListDto.getListName())) {
				response.getArticleList().add(articleListToAdd);
				this.validateUser(userMapper.userToUserDto(response));
				this.userRepository.save(response);
			} else {
				throw new ValidationException(
						"Error validation: listname " + articleListDto.getListName() + " must not be repeated.");
			}
		}, () -> {
			User newUserToAdd = new User(username, List.of(articleListToAdd));
			this.validateUser(userMapper.userToUserDto(newUserToAdd));
			this.userRepository.save(newUserToAdd);
		});
		return new ResponseEntity("List " + articleListDto.getListName() + " added successfully for user " + username,
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> addArticleToList(String username, String listname, Integer articleId) {
		Optional<User> user = this.userRepository.findById(username);
		user.ifPresentOrElse(response -> {
			if (UserUtils.articleListContainsName(response.getArticleList(), listname)) {
				response.getArticleList().stream().filter(obj -> obj.getListName().equals(listname)).findFirst().get()
						.getIdArticleList().add(articleId);
				this.validateUser((userMapper.userToUserDto(response)));
				this.userRepository.save(response);
			} else {
				throw new ValidationException(
						"Error validation: listname " + listname + " not exists for user : " + username);
			}
		}, () -> {
			throw new ValidationException("Error validation: username " + username + " don't exists.");
		});
		return new ResponseEntity(
				"Article " + articleId + " added successfully to list " + listname + " for user " + username,
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> deleteList(String username, String listname) {
		Optional<User> user = this.userRepository.findById(username);
		user.ifPresentOrElse(response -> {
			if (UserUtils.articleListContainsName(response.getArticleList(), listname)) {
				response.getArticleList().remove(response.getArticleList().stream()
						.filter(obj -> obj.getListName().equals(listname)).findFirst().get());
				if (response.getArticleList().isEmpty()) {
					this.userRepository.delete(response);
				} else {
					this.validateUser((userMapper.userToUserDto(response)));
					this.userRepository.save(response);
				}

			} else {
				throw new ValidationException(
						"Error validation: listname " + listname + " not exists for user : " + username);
			}
		}, () -> {
			throw new ValidationException("Error validation: username " + username + " don't exists.");
		});
		return new ResponseEntity("List " + listname + " removed successfully for user " + username, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> deleteArticleFromList(String username, String listname, Integer articleId) {
		Optional<User> user = this.userRepository.findById(username);
		user.ifPresentOrElse(response -> {
			if (UserUtils.articleListContainsName(response.getArticleList(), listname)) {
				ArticleList articleList = response.getArticleList().stream()
						.filter(obj -> obj.getListName().equals(listname)).findFirst().get();
				if (articleList.getIdArticleList().contains(articleId)) {
					articleList.getIdArticleList().remove(articleId);
					if (articleList.getIdArticleList().isEmpty()) {
						response.getArticleList().remove(articleList);
					}
					if (response.getArticleList().isEmpty()) {
						this.userRepository.delete(response);
					} else {
						this.validateUser((userMapper.userToUserDto(response)));
						this.userRepository.save(response);
					}
				} else {
					throw new ValidationException(
							"Error validation: listname " + listname + " don't contain articleId: " + articleId);
				}
			} else {
				throw new ValidationException(
						"Error validation: listname " + listname + " not exists for user : " + username);
			}
		}, () -> {
			throw new ValidationException("Error validation: username " + username + " don't exists.");
		});
		return new ResponseEntity(
				"Article " + articleId + " removed successfully from list " + listname + " for user " + username,
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getListDetailByListname(String username, String listname) {
		Optional<User> user = this.userRepository.findByUsernameAndArticleList_listName(username, listname);
		if(user.isPresent()) {
			ArticleList articleList = user.get().getArticleList().stream().filter(obj -> obj.getListName().equals(listname)).findFirst().orElse(null);
			ArticleResponseDto articleResponseDto = null;
			if(articleList!= null) {
				articleResponseDto = new ArticleResponseDto();
				articleResponseDto.setListName(articleList.getListName());
				List<ProductDto> productList = new ArrayList<ProductDto>();
				for(Integer idArticle : articleList.getIdArticleList()) {
					 Mono<String> productMono = client.get()
					            .uri("/product/{id}", idArticle)
					            .accept(MediaType.APPLICATION_JSON)
					            .retrieve()
					            .bodyToMono(String.class);
					 productMono.subscribe(product ->{
						 try {
							productList.add(objectMapper.readValue(product, ProductDto.class));
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}
					 });
				}
				articleResponseDto.setIdArticleList(productList);
			}else {
				throw new ValidationException(
						"Error validation: listname " + listname + " not exists for user : " + username);
			}
			return new ResponseEntity(articleResponseDto, HttpStatus.OK);
		}else {
			throw new ValidationException("Error validation: username " + username + "or listname:" + listname +" don't exists.");
		}
		
	}
	
	@Override
	public ResponseEntity<?> getAllListsFromUser(String username) {
		Optional<User> user = this.userRepository.findById(username);
		if(user.isPresent()) {
			return new ResponseEntity(user.get(), HttpStatus.OK);
		}else {
			throw new ValidationException("Error validation: username " + username + " don't exists.");
		}
		
	}

	private void validateUser(UserDto response) {
		Set<ConstraintViolation<UserDto>> violations = validator.validate(response);
		if (!violations.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (ConstraintViolation<UserDto> constraintViolation : violations) {
				sb.append(constraintViolation.getMessage());
			}
			throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
		}
	}

}
