package com.demo.controller;

import com.demo.model.User;
import com.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public Optional<User> getUser(@PathVariable String name)
    {
        return userService.getUser(name);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@Validated @RequestBody User userData) {
        User user = userService.createUser(userData);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

}
