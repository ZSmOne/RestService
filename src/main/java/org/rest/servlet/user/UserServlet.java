package org.rest.servlet.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.rest.model.User;
import org.rest.service.UserService;
import org.rest.servlet.user.dto.UserIncomingDto;
import org.rest.servlet.mapper.UserMapper;
import org.rest.exception.NotFoundException;
import org.rest.servlet.user.dto.UserOutGoingDto;
import org.rest.servlet.user.dto.UserUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserServlet {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserServlet(UserService userService) {
        this.userService = userService;
        this.objectMapper = new ObjectMapper();
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getUserById(@PathVariable("id") Long userId) {
        try {
            User user = userService.findById(userId);
            UserOutGoingDto userDto = UserMapper.INSTANCE.userToUserOutGoingDto(user);
            String userJson = objectMapper.writeValueAsString(userDto);
            return ResponseEntity.ok(userJson);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<String> getAllUsers() {
        try {
            List<User> users = userService.findAll();
            List<UserOutGoingDto> userOutGoingDtos = users.stream()
                    .map(UserMapper.INSTANCE::userToUserOutGoingDto)
                    .toList();
            String usersJson = objectMapper.writeValueAsString(userOutGoingDtos);
            return ResponseEntity.ok(usersJson);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request.");
        }
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody String json) {
        try {
            UserIncomingDto userResponse = objectMapper.readValue(json, UserIncomingDto.class);
            if (userResponse.getName() == null || userResponse.getName().isEmpty()) {
                return ResponseEntity.badRequest().body("Name field is required.");
            }
            User user = UserMapper.INSTANCE.userIncomingDtoToUser(userResponse);
            user = userService.save(user);
            UserOutGoingDto savedUserDto= UserMapper.INSTANCE.userToUserOutGoingDto(user);
            String userJson = objectMapper.writeValueAsString(savedUserDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(userJson);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect user Object.");
        }
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody String json) {
        try {
            UserUpdateDto userDto = objectMapper.readValue(json, UserUpdateDto.class);
            User user = UserMapper.INSTANCE.userUpdateDtoToUser(userDto);
            userService.update(user);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect user Object.");
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
        try {
            userService.delete(userId);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request.");
        }
    }
}