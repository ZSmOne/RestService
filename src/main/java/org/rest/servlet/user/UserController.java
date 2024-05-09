package org.rest.servlet.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.rest.exception.NotFoundException;
import org.rest.model.User;
import org.rest.service.impl.UserServiceImpl;
import org.rest.servlet.mapper.UserMapper;
import org.rest.servlet.user.dto.UserIncomingDto;
import org.rest.servlet.user.dto.UserOutGoingDto;
import org.rest.servlet.user.dto.UserUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserServiceImpl userServiceImpl;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.objectMapper = new ObjectMapper();
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getUserById(@PathVariable("id") Long userId) {
        try {
            User user = userServiceImpl.findById(userId);
            UserOutGoingDto userDto = UserMapper.INSTANCE.userToUserOutGoingDto(user);
            String userJson = objectMapper.writeValueAsString(userDto);
            return ResponseEntity.ok(userJson);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (JsonProcessingException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<String> getAllUsers() {
        try {
            List<User> users = userServiceImpl.findAll();
            List<UserOutGoingDto> userOutGoingDtoList = users.stream()
                    .map(UserMapper.INSTANCE::userToUserOutGoingDto)
                    .toList();
            String usersJson = objectMapper.writeValueAsString(userOutGoingDtoList);
            return ResponseEntity.ok(usersJson);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Bad request.");
        }
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody UserUpdateDto userDto) {
        try {
            User user = UserMapper.INSTANCE.userUpdateDtoToUser(userDto);
            userServiceImpl.update(user);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect user Object.");
        }
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserIncomingDto userResponse) {
        try {
            if (userResponse.getName() == null || userResponse.getName().isEmpty()) {
                return ResponseEntity.badRequest().body("Name field is required.");
            }
            User user = UserMapper.INSTANCE.userIncomingDtoToUser(userResponse);
            user = userServiceImpl.save(user);
            UserOutGoingDto savedUserDto= UserMapper.INSTANCE.userToUserOutGoingDto(user);
            String userJson = objectMapper.writeValueAsString(savedUserDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(userJson);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect user Object.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
        try {
            userServiceImpl.delete(userId);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handle(Throwable e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}