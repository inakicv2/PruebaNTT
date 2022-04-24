package com.everis.hello;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import com.everis.hello.controller.UserListsController;


@EnableAutoConfiguration
public class UserListsServiceFT {
	
	@InjectMocks
	private UserListsController userListsController;

	@BeforeEach
	public void onBefore() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void sayHello_shouldReturnNameOK() {

		
	}


}
