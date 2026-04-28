package com.stackoverflow.backend.controller;

import com.stackoverflow.backend.entity.User;
import com.stackoverflow.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "${app.frontend.url}")
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

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        Optional<User> authenticatedUser = userService.authenticateUser(email, password);

        if (authenticatedUser.isPresent()) {

            return ResponseEntity.ok(authenticatedUser.get());
        } else {
            // send 401 to angular
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect credentials");
        }
    }
}

