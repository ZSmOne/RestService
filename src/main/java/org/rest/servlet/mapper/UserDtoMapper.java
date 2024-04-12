package org.rest.servlet.mapper;

import org.rest.model.User;
import org.rest.servlet.dto.UserIncomingDto;
import org.rest.servlet.dto.UserOutGoingDto;
import org.rest.servlet.dto.UserUpdateDto;

import java.util.List;

public interface UserDtoMapper {
    User map(UserIncomingDto userIncomingDto);

    User map(UserUpdateDto userIncomingDto);

    UserOutGoingDto map(User user);

    List<UserOutGoingDto> map(List<User> user);

}
