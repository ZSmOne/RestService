package org.rest.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rest.exception.NotFoundException;
import org.rest.model.City;
import org.rest.model.User;
import org.rest.repository.UserCrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)

class UserServiceImplTest {
    @Mock
    private UserCrudRepository userRepository;

    @Mock
    private CityServiceImpl cityService;

    @InjectMocks
    private UserServiceImpl userService;



    @Test
    void testSave() {
        City city = new City(1L, "city");
        User user = new User(1L, "TestCity", city, List.of());
        when(userRepository.save(user)).thenReturn(user);

        Assertions.assertEquals(user, userService.save(user));
    }
    @Test
    void testFindById() throws NotFoundException {
        City city = new City(1L, "city");
        User user = new User(1L, "TestCity", city, List.of());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User userFind = userService.findById(1L);
        Assertions.assertEquals(user.getId(), userService.findById(1L).getId());
        Assertions.assertEquals(user.getId(), userFind.getId());
    }


    @Test
    void testFindAll() {
        List<User> userList = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(userList);
        Assertions.assertEquals(userList, userService.findAll());
    }

    @Test
    void testUpdate() throws NotFoundException {
        City city = new City(1L, "city");
        User user = new User(1L, "TestCity", city, List.of());
        when(userRepository.existsById(1L)).thenReturn(true);
        userService.update(user);

        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDelete() throws NotFoundException {
        City city = new City(1L, "city");
        User user = new User(1L, "TestCity", city, List.of());
        when(userRepository.existsById(1L)).thenReturn(true);
        userService.delete(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
    @Test
    void testGetUser_Success() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        User result = userService.getUser(userId);

        verify(userRepository, times(1)).findById(userId);
        Assertions.assertEquals(user, result);
    }
}
