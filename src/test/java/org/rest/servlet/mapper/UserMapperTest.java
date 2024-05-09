package org.rest.servlet.mapper;

import org.junit.jupiter.api.Test;
import org.rest.model.City;
import org.rest.model.User;
import org.rest.servlet.user.dto.UserIncomingDto;
import org.rest.servlet.user.dto.UserOutGoingDto;
import org.rest.servlet.user.dto.UserSimpleOutGoingDto;
import org.rest.servlet.user.dto.UserUpdateDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    @Test
    void testUserToUserSimpleOutGoingDto() {
        User user = new User(1L, "Test User", new City(1L, "Test City"), null);
        UserSimpleOutGoingDto dto = UserMapper.INSTANCE.userToUserSimpleOutGoingDto(user);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getName(), dto.getName());
    }

    @Test
    void testUserIncomingDtoToUser() {
        UserIncomingDto dto = new UserIncomingDto();
        dto.setName("Test User");
        User user = UserMapper.INSTANCE.userIncomingDtoToUser(dto);
        assertEquals(dto.getName(), user.getName());
    }

    @Test
    void testUserUpdateDtoToUser() {
        UserUpdateDto dto = new UserUpdateDto(1L, "Updated User", null);
        User user = UserMapper.INSTANCE.userUpdateDtoToUser(dto);
        assertEquals(dto.getId(), user.getId());
        assertEquals(dto.getName(), user.getName());
    }

    @Test
    void testUserToUserOutGoingDto() {
        User user = new User(1L, "Test User", new City(1L, "Test City"), null);
        UserOutGoingDto dto = UserMapper.INSTANCE.userToUserOutGoingDto(user);
        assertEquals(user.getName(), dto.getName());
    }
}
