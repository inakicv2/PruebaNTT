package com.everis.hello;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
	private static final String TEST_USER2 = "testuser2";
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
	public void createListOk25Items() {
		ArticleListDto articleListDto = new ArticleListDto();
		articleListDto.setListName(TEST_LIST);
		articleListDto.setIdArticleList(List.of(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,1,2,3,4,5,6,7,8,9,10));
		userListsService.createList(TEST_USER, articleListDto);
		List<User> userList = this.userRepository.findAll();
		assertEquals(userList.size(), 1);
		assertEquals(userList.get(0).getArticleList().size(), 1);
		assertEquals(userList.get(0).getArticleList().get(0).getIdArticleList().size(), 25);
	}
	
	@Test
	public void createListOk1Item() {
		ArticleListDto articleListDto = new ArticleListDto();
		articleListDto.setListName(TEST_LIST);
		articleListDto.setIdArticleList(List.of(1));
		userListsService.createList(TEST_USER, articleListDto);
		List<User> userList = this.userRepository.findAll();
		assertEquals(userList.size(), 1);
		assertEquals(userList.get(0).getArticleList().size(), 1);
		assertEquals(userList.get(0).getArticleList().get(0).getIdArticleList().size(), 1);
	}
	
	@Test
	public void createListKoNoListname() {
		ArticleListDto articleListDto = new ArticleListDto();
		articleListDto.setIdArticleList(List.of(1,2,3,4,5));
		ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, ()->{
			userListsService.createList(TEST_USER, articleListDto);
		});
		assertTrue(thrown.getMessage().contains("no debe estar vacío"));
		List<User> userList = this.userRepository.findAll();
		assertEquals(userList.size(), 0);
	}
	
	@Test
	public void createListKoNullArticleList() {
		ArticleListDto articleListDto = new ArticleListDto();
		articleListDto.setListName(TEST_LIST);
		ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, ()->{
			userListsService.createList(TEST_USER, articleListDto);
		});
		assertTrue(thrown.getMessage().contains("no debe estar vacío"));
		List<User> userList = this.userRepository.findAll();
		assertEquals(userList.size(), 0);
	}
	
	@Test
	public void createListKoArticleListIdNotValidMax() {
		ArticleListDto articleListDto = new ArticleListDto();
		articleListDto.setListName(TEST_LIST);
		articleListDto.setIdArticleList(List.of(16));
		ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, ()->{
			userListsService.createList(TEST_USER, articleListDto);
		});
		assertTrue(thrown.getMessage().contains("debe ser menor que o igual a 15"));
		List<User> userList = this.userRepository.findAll();
		assertEquals(userList.size(), 0);
	}
	
	@Test
	public void createListKoArticleListIdNotValidMin() {
		ArticleListDto articleListDto = new ArticleListDto();
		articleListDto.setListName(TEST_LIST);
		articleListDto.setIdArticleList(List.of(0));
		ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, ()->{
			userListsService.createList(TEST_USER, articleListDto);
		});
		assertTrue(thrown.getMessage().contains("debe ser mayor que o igual a 1"));
		List<User> userList = this.userRepository.findAll();
		assertEquals(userList.size(), 0);
	}
	
	@Test
	public void createListKoArticleListIdNotValidSize() {
		ArticleListDto articleListDto = new ArticleListDto();
		articleListDto.setListName(TEST_LIST);
		articleListDto.setIdArticleList(List.of(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,1,2,3,4,5,6,7,8,9,10,11));
		ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, ()->{
			userListsService.createList(TEST_USER, articleListDto);
		});
		assertTrue(thrown.getMessage().contains("el tamaño debe estar entre 1 y 25"));
		List<User> userList = this.userRepository.findAll();
		assertEquals(userList.size(), 0);
	}
	
	@Test
	public void createListKoNotValidListsSize() {
		this.create5ListOk();
		ArticleListDto articleListDto = new ArticleListDto();
		articleListDto.setListName(TEST_LIST6);
		articleListDto.setIdArticleList(List.of(1,2,3));
		ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, ()->{
			userListsService.createList(TEST_USER, articleListDto);
		});
		assertTrue(thrown.getMessage().contains("el tamaño debe estar entre 1 y 5"));
		List<User> userList = this.userRepository.findAll();
		assertEquals(userList.get(0).getArticleList().size(),5);
	}
	
	@Test
	public void createListFor2UsersOk() {
		ArticleListDto articleListDto = new ArticleListDto();
		articleListDto.setListName(TEST_LIST);
		articleListDto.setIdArticleList(List.of(1,2,3,4,5));
		userListsService.createList(TEST_USER, articleListDto);
		articleListDto.setListName(TEST_LIST2);
		userListsService.createList(TEST_USER2, articleListDto);
		List<User> userList = this.userRepository.findAll();
		assertEquals(userList.size(), 2);
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
	public void create2ListOk() {
		//list1
		ArticleListDto articleListDto = new ArticleListDto();
		articleListDto.setListName(TEST_LIST);
		articleListDto.setIdArticleList(List.of(1));
		userListsService.createList(TEST_USER, articleListDto);
		//list2
		articleListDto.setListName(TEST_LIST2);
		userListsService.createList(TEST_USER, articleListDto);
		List<User> userList = this.userRepository.findAll();
		assertEquals(userList.size(), 1);
		assertEquals(userList.get(0).getArticleList().size(), 2);
	}
	
	@Test
	public void addArticleOk() {
		this.createListOk();
		userListsService.addArticleToList(TEST_USER, TEST_LIST, 1);
		List<User> userList = this.userRepository.findAll();
		assertEquals(userList.get(0).getArticleList().get(0).getIdArticleList().size(), 6);
	}
	
	@Test
	public void addArticleKoListname() {
		this.createListOk();		
		ValidationException thrown = assertThrows(ValidationException.class, ()->{
			userListsService.addArticleToList(TEST_USER, TEST_LIST2, 1);
		});
		assertTrue(thrown.getMessage().contains("Error validation: listname"));
	}
	
	@Test
	public void addArticleKoUsername() {
		this.createListOk();		
		ValidationException thrown = assertThrows(ValidationException.class, ()->{
			userListsService.addArticleToList(TEST_USER2, TEST_LIST, 1);
		});
		assertTrue(thrown.getMessage().contains("Error validation: username"));
	}
	
	@Test
	public void addArticleKoArticleListIdNotValidSize() {
		this.createListOk25Items();
		ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, ()->{
			userListsService.addArticleToList(TEST_USER, TEST_LIST, 1);
		});
		assertTrue(thrown.getMessage().contains("el tamaño debe estar entre 1 y 25"));
		List<User> userList = this.userRepository.findAll();
		assertEquals(userList.get(0).getArticleList().get(0).getIdArticleList().size(), 25);
	}
		
	@Test
	public void deleteListOk() {
		this.create5ListOk();
		userListsService.deleteList(TEST_USER, TEST_LIST);
		List<User> userList = this.userRepository.findAll();
		assertEquals(userList.get(0).getArticleList().size(), 4);
	}
	
	@Test
	public void deleteListOkDeleteDocumentWhileEmpty() {
		this.createListOk();
		userListsService.deleteList(TEST_USER, TEST_LIST);
		List<User> userList = this.userRepository.findAll();
		assertEquals(userList.size(), 0);
	}
	
	@Test
	public void deleteListKoListname() {
		this.createListOk();		
		ValidationException thrown = assertThrows(ValidationException.class, ()->{
			userListsService.deleteList(TEST_USER, TEST_LIST2);
		});
		assertTrue(thrown.getMessage().contains("Error validation: listname"));
	}
	
	@Test
	public void deleteListKoUsername() {
		this.createListOk();		
		ValidationException thrown = assertThrows(ValidationException.class, ()->{
			userListsService.deleteList(TEST_USER2, TEST_LIST);
		});
		assertTrue(thrown.getMessage().contains("Error validation: username"));
	}
	
	@Test
	public void deleteArticleFormListOk() {
		this.createListOk();
		userListsService.deleteArticleFromList(TEST_USER, TEST_LIST, 1);
		List<User> userList = this.userRepository.findAll();
		assertEquals(userList.get(0).getArticleList().get(0).getIdArticleList().size(), 4);
	}
	
	@Test
	public void deleteArticleFormListOkRemoveListWhileEmpty() {
		this.create2ListOk();
		userListsService.deleteArticleFromList(TEST_USER, TEST_LIST, 1);
		List<User> userList = this.userRepository.findAll();
		assertEquals(userList.get(0).getArticleList().size(), 1);
	}

	@Test
	public void deleteArticleFormListOkRemoveDocumentWhileEmpty() {
		this.create2ListOk();
		userListsService.deleteArticleFromList(TEST_USER, TEST_LIST, 1);
		userListsService.deleteArticleFromList(TEST_USER, TEST_LIST2, 1);
		List<User> userList = this.userRepository.findAll();
		assertEquals(userList.size(), 0);
	}
	
	@Test
	public void deleteArticleFormListKoListNoContainArticleId() {
		this.createListOk();		
		ValidationException thrown = assertThrows(ValidationException.class, ()->{
			userListsService.deleteArticleFromList(TEST_USER, TEST_LIST,6);
		});
		assertTrue(thrown.getMessage().contains("don't contain articleId"));
	}
	
	@Test
	public void deleteArticleFormListKoListname() {
		this.createListOk();		
		ValidationException thrown = assertThrows(ValidationException.class, ()->{
			userListsService.deleteArticleFromList(TEST_USER, TEST_LIST2,1);
		});
		assertTrue(thrown.getMessage().contains("Error validation: listname"));
	}
	
	@Test
	public void deleteArticleFormListKoUsername() {
		this.createListOk();		
		ValidationException thrown = assertThrows(ValidationException.class, ()->{
			userListsService.deleteArticleFromList(TEST_USER2, TEST_LIST,1);
		});
		assertTrue(thrown.getMessage().contains("Error validation: username"));
	}
	
	@Test
	public void getAllListsFromUserOk() {
		this.createListOk();
		ResponseEntity<?> response = userListsService.getAllListsFromUser(TEST_USER);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	public void getAllListsFromUserKoUsername() {
		this.createListOk();		
		ValidationException thrown = assertThrows(ValidationException.class, ()->{
			userListsService.getAllListsFromUser(TEST_USER2);
		});
		assertTrue(thrown.getMessage().contains("Error validation: username"));
	}
	
	@Test
	public void getListDetailByListnameOk() {
		this.createListOk();
		ResponseEntity<?> response = userListsService.getListDetailByListname(TEST_USER,TEST_LIST);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertThat(response.getBody().toString().contains("{\r\n"
				+ "  \"id\": 1,\r\n"
				+ "  \"name\": \"Jacket\",\r\n"
				+ "  \"size\": \"XL\",\r\n"
				+ "  \"price\": \"19.99\",\r\n"
				+ "  \"color\": \"RED\"\r\n"
				+ "}"));
	}
	
	@Test
	public void getListDetailByListnameKoUsername() {
		this.createListOk();		
		ValidationException thrown = assertThrows(ValidationException.class, ()->{
			userListsService.getListDetailByListname(TEST_USER2, TEST_LIST);
		});
		assertTrue(thrown.getMessage().contains("Error validation: username"));
	}
	
}
