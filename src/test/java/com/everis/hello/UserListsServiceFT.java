package com.everis.hello;


import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.everis.hello.dto.ArticleListDto;
import com.everis.hello.model.User;
import com.everis.hello.repository.UserRepository;
import com.everis.hello.service.UserListsService;

@SpringBootTest
@EnableAutoConfiguration
public class UserListsServiceFT {
	
	@SpyBean
	private UserListsService userListsService;
	
	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	public void onBefore() {
		MockitoAnnotations.openMocks(this);
		this.userRepository.deleteAll();
	} 
	
	private static final String TEST_USER = "testuser";
	private static final String TEST_LIST = "testList";
	private static final String TEST_LIST2 = "testList2";
	private static final String TEST_LIST3 = "testList3";
	private static final String TEST_LIST4 = "testList4";
	private static final String TEST_LIST5 = "testList5";
	private static final String TEST_LIST6 = "testList6";
	
	@Test
	public void createListOk() {
		ArticleListDto articleListDto = new ArticleListDto();
		articleListDto.setListName(TEST_LIST);
		articleListDto.setIdArticleList(List.of(1,2,3,4,5));
		userListsService.createList(TEST_USER, articleListDto);
		List<User> userList = this.userRepository.findAll();
		assertEquals(userList.size(), 1);
		assertEquals(userList.get(0).getArticleList().size(), 1);
		assertEquals(userList.get(0).getArticleList().get(0).getIdArticleList().size(), 5);
	}
	
	@Test
	public void create5ListOk() {
		//list1
		ArticleListDto articleListDto = new ArticleListDto();
		articleListDto.setListName(TEST_LIST);
		articleListDto.setIdArticleList(List.of(1,2,3,4,5));
		userListsService.createList(TEST_USER, articleListDto);
		//list2
		articleListDto.setListName(TEST_LIST2);
		userListsService.createList(TEST_USER, articleListDto);
		//list3
		articleListDto.setListName(TEST_LIST3);
		userListsService.createList(TEST_USER, articleListDto);
		//list4
		articleListDto.setListName(TEST_LIST4);
		userListsService.createList(TEST_USER, articleListDto);
		//list5
		articleListDto.setListName(TEST_LIST5);
		userListsService.createList(TEST_USER, articleListDto);
		List<User> userList = this.userRepository.findAll();
		assertEquals(userList.size(), 1);
		assertEquals(userList.get(0).getArticleList().size(), 5);
	}
	
	@Test
	public void addArticleOk() {
		this.createListOk();
		userListsService.addArticleToList(TEST_USER, TEST_LIST, 1);
		List<User> userList = this.userRepository.findAll();
		assertEquals(userList.get(0).getArticleList().get(0).getIdArticleList().size(), 6);
	}
		
	@Test
	public void deleteListOk() {
		this.create5ListOk();
		userListsService.deleteList(TEST_USER, TEST_LIST);
		List<User> userList = this.userRepository.findAll();
		assertEquals(userList.get(0).getArticleList().size(), 4);
	}
	
	@Test
	public void deleteArticleFormListOk() {
		this.createListOk();
		userListsService.deleteArticleFromList(TEST_USER, TEST_LIST, 1);
		List<User> userList = this.userRepository.findAll();
		assertEquals(userList.get(0).getArticleList().get(0).getIdArticleList().size(), 4);
	}
	


}
