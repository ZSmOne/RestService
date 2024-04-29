package org.vlad.rest.servlet.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rest.exception.NotFoundException;
import org.rest.model.User;
import org.rest.service.UserService;
import org.rest.servlet.user.UserServlet;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserServlet userServlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetUserById() throws IOException, NotFoundException {
        Long userId = 1L;
        User user = new User(userId, "Test User", null, null);
        when(userService.findById(userId)).thenReturn(user);
        ResponseEntity<String> response = userServlet.getUserById(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetUserById_NotFound() throws IOException, NotFoundException {
        Long userId = 1L;
        when(userService.findById(userId)).thenThrow(new NotFoundException("User not found"));
        ResponseEntity<String> response = userServlet.getUserById(userId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllUsers() throws IOException {
        // Arrange
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "User 1", null, null));
        users.add(new User(2L, "User 2", null, null));
        when(userService.findAll()).thenReturn(users);
        ResponseEntity<String> response = userServlet.getAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetAllUsers_BadRequest() throws IOException {
        when(userService.findAll()).thenThrow(new IllegalArgumentException("Bad request"));
        ResponseEntity<String> response = userServlet.getAllUsers();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testCreateUser_BadRequest() throws IOException {
        String wrongJson = "{}";
        ResponseEntity<String> response = userServlet.createUser(wrongJson);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testCreateUser() throws IOException {
        String json = "{\"name\":\"Test User\"}";
        User user = new User(1L, "Test User", null, null);
        when(userService.save(any())).thenReturn(user);
        ResponseEntity<String> response = userServlet.createUser(json);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testUpdateUser() throws IOException, NotFoundException {
        String json = "{\"id\":2,\"name\":\"Updated User,\"city\":{\"id\": 1}}";
        doNothing().when(userService).update(any());
        ResponseEntity<String> response = userServlet.updateUser(json);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateUser_NotFound() throws IOException, NotFoundException {
        // Arrange
        String json = "{\"id\":1,\"name\":\"Updated User\"}";
        doThrow(new NotFoundException("User not found")).when(userService).update(any());
        ResponseEntity<String> response = userServlet.updateUser(json);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteUser() throws NotFoundException {
        Long userId = 1L;
        doNothing().when(userService).delete(userId);
        ResponseEntity<String> response = userServlet.deleteUser(userId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteUser_NotFound() throws NotFoundException {
        Long userId = 1L;
        doThrow(new NotFoundException("User not found")).when(userService).delete(userId);
        ResponseEntity<String> response = userServlet.deleteUser(userId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
