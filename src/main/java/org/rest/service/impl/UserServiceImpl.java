package org.rest.service.impl;

import org.rest.model.City;
import org.rest.model.User;
import org.rest.repository.UserCrudRepository;
import org.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService {
    private final UserCrudRepository userCrudRepository;
    private final CityServiceImpl cityServiceImpl;

    @Autowired
    public UserServiceImpl(CityServiceImpl cityServiceImpl, UserCrudRepository userCrudRepository) {
        this.userCrudRepository = userCrudRepository;
        this.cityServiceImpl = cityServiceImpl;
    }


    @Override
    public User save(User user){
        return userCrudRepository.save(user);
    }

    @Override
    public User findById(Long id){
        return userCrudRepository.findById(id).orElseThrow(() -> new IllegalStateException("no such bank"));
    }

    @Override
    public List<User> findAll(){
        Iterable<User> iterable = userCrudRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                .toList();
    }

    @Override
    public void update(User user) {
        if (userCrudRepository.existsById(user.getId())) {
            City city = cityServiceImpl.findById (user.getCity().getId());
            user.setCity(city);
            userCrudRepository.save(user);
        }
    }

    @Override
    public void delete(Long id) {
        if (userCrudRepository.existsById(id))
            userCrudRepository.deleteById(id);
    }

    public User getUser(Long id) {
        return userCrudRepository.findById(id).orElseThrow(() -> new IllegalStateException("User not found"));
    }
}
