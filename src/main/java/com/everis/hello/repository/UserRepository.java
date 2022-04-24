package com.everis.hello.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.everis.hello.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
	
	public Optional<User> findByUsernameAndArticleList_listName(String username, String listname);

}
