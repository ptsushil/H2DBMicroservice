package com.demo.service;


import com.demo.model.User;
import com.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    public Optional<User> getUser(Long id)
    {
        return userRepository.findById(id);
    }
    public Optional<User> getUserByName(String name)
    {
        return userRepository.findByUserName(name);
    }
    public User createUser(User data) {
        // Make sure we aren't getting a request to create something that already exists
        if (userRepository.findById(data.getId()).isPresent() ||
                data.getUserName() != null && userRepository.findByUserName(data.getUserName()).isPresent()) {
            // Can't create a user that already exists
            throw new RuntimeException("User already exists.");
        }
        return userRepository.save(data);
    }
}
