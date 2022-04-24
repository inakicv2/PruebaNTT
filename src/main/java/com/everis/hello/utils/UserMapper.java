package com.everis.hello.utils;

import org.mapstruct.Mapper;
import com.everis.hello.dto.UserDto;
import com.everis.hello.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
	UserDto userToUserDto(User user);
    
    User userDtoToUser(UserDto userDto);

}
