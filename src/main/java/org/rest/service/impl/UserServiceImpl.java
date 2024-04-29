package org.rest.service.impl;

import org.rest.exception.NotFoundException;
import org.rest.model.User;
import org.rest.repository.UserRepository;
import org.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    private UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        user = userRepository.save(user);
        return user;
    }

    @Override
    public User findById(Long userId) throws NotFoundException {
        User user = userRepository.findById(userId);
        if (user == null)
            throw new NotFoundException("User not found.");
        return user;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();    }

    @Override
    public void update(User user) throws NotFoundException {
        isExistUser(user.getId());
        userRepository.update(user);
    }

    @Override
    public boolean delete(Long userId) throws NotFoundException {
        isExistUser(userId);
        return userRepository.deleteById(userId);
    }


    private void isExistUser(Long userId) throws NotFoundException {
        if (!userRepository.existById(userId)) {
            throw new NotFoundException("User not found.");
        }
    }
}
