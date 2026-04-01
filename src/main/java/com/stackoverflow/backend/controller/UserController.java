package com.stackoverflow.backend.controller;

import com.stackoverflow.backend.entity.User;
import com.stackoverflow.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //  create
    @PostMapping
    public User save(@RequestBody User user) {
        return userService.createUser(user);
    }

    //  read all
    @GetMapping
    public List<User> findAll() {
        return userService.getAllUsers();
    }

    //  read by id
    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    //  update
    @PutMapping("/{id}")
    public User update(@RequestBody User user, @PathVariable Long id) {
        return userService.updateUser(user, id);
    }

    //  delete
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        userService.deleteUser(id);
    }


}

