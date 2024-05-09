package org.rest.servlet.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rest.exception.NotFoundException;
import org.rest.model.User;
import org.rest.service.impl.UserServiceImpl;
import org.rest.servlet.user.dto.UserIncomingDto;
import org.rest.servlet.user.dto.UserUpdateDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetUser() throws NotFoundException {
        Long userId = 1L;
        User user = new User(userId, "Test User", null, null);
        when(userService.findById(userId)).thenReturn(user);
        ResponseEntity<String> response = userController.getUserById(userId);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetUserById_NotFound() throws NotFoundException {
        Long userId = 1L;
        when(userService.findById(userId)).thenThrow(new NotFoundException("User not found"));
        ResponseEntity<String> response = userController.getUserById(userId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "User 1", null, null));
        users.add(new User(2L, "User 2", null, null));
        when(userService.findAll()).thenReturn(users);
        ResponseEntity<String> response = userController.getAllUsers();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testCreateUser() {
        User user = new User(1L, "Test User", null, null);
        UserIncomingDto userDto = new UserIncomingDto("Test User", null);
        when(userService.save(any())).thenReturn(user);
        ResponseEntity<String> response = userController.createUser(userDto);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testUpdateUser() throws NotFoundException {
        UserUpdateDto userUpdateDto = new UserUpdateDto(2L, "Updated User", null);
        doNothing().when(userService).update(any());
        ResponseEntity<String> response = userController.updateUser(userUpdateDto);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateUser_NotFound() throws NotFoundException {
        UserUpdateDto userUpdateDto = new UserUpdateDto(1L, "Updated User", null);
        doThrow(new NotFoundException("User not found")).when(userService).update(any());
        ResponseEntity<String> response = userController.updateUser(userUpdateDto);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteUser() throws NotFoundException {
        Long userId = 1L;
        doNothing().when(userService).delete(userId);
        ResponseEntity<String> response = userController.deleteUser(userId);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteUser_NotFound() throws NotFoundException {
        Long userId = 1L;
        doThrow(new NotFoundException("User not found")).when(userService).delete(userId);
        ResponseEntity<String> response = userController.deleteUser(userId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
