package org.rest.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rest.exception.NotFoundException;
import org.rest.model.City;
import org.rest.model.User;
import org.rest.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void testSaveCity() {
        City city = new City(1L, "city");

        User user = new User(1L, "TestCity", city, List.of());
        when(userRepository.save(user)).thenReturn(user);

        User savedCity = userService.save(user);

        assertNotNull(savedCity);
        assertEquals(user.getId(), savedCity.getId());
        assertEquals(user.getName(), savedCity.getName());
    }
    @Test
    void testSave() {
        City city = new City(1L, "city");

        User user = new User(1L, "TestCity", city, List.of());

        when(userRepository.save(user)).thenReturn(user);
        assertEquals(user, userService.save(user));
    }
    @Test
    void testFindById_UserExists() throws NotFoundException {
        City city = new City(1L, "city");
        User user = new User(1L, "TestCity", city, List.of());
        when(userRepository.findById(1L)).thenReturn(user);

        User userFind = userService.findById(1L);
        assertEquals(user.getId(), userService.findById(1L).getId());
        assertEquals(user.getId(), userFind.getId());
    }

    @Test
    void testFindById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void testFindAll() {
        List<User> userList = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(userList);
        assertEquals(userList, userService.findAll());
    }

    @Test
    void testUpdate_UserExists() throws NotFoundException {
        City city = new City(1L, "city");
        User user = new User(1L, "TestCity", city, List.of());
        when(userRepository.existById(1L)).thenReturn(true);
        userService.update(user);

        verify(userRepository, times(1)).update(user);
    }

    @Test
    void testUpdate_UserNotFound() {
        City city = new City(1L, "city");
        User user = new User(1L, "TestCity", city, List.of());
        when(userRepository.existById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.update(user));
    }

    @Test
    void testDelete_UserExists() throws NotFoundException {
        City city = new City(1L, "city");
        User user = new User(1L, "TestCity", city, List.of());
        when(userRepository.deleteById(1L)).thenReturn(true);
        when(userRepository.existById(1L)).thenReturn(true);

        assertTrue(userService.delete(1L));
    }

    @Test
    void testDelete_UserNotFound() {
        when(userRepository.existById(1L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> userService.delete(1L));
    }
}
