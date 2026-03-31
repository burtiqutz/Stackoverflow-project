package com.stackoverflow.backend.service;

import com.stackoverflow.backend.entity.User;
import com.stackoverflow.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_ShouldSaveUser_WhenDataIsValid() {
        // ARRANGE
        User user = new User();
        user.setUsername("gigel");
        user.setEmail("gigel@test.com");

        // return no for the user+email
        when(userRepository.existsByUsername("gigel")).thenReturn(false);
        when(userRepository.existsByEmail("gigel@test.com")).thenReturn(false);

        // return user
        when(userRepository.save(user)).thenReturn(user);

        // act
        User savedUser = userService.createUser(user);

        // assert
        assertNotNull(savedUser);
        assertEquals("gigel", savedUser.getUsername());
        // check
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUser_ShouldThrowException_WhenUsernameAlreadyExists() {
        // ARRANGE
        User user = new User();
        user.setUsername("hacker");

        //  tell the mock username exists
        when(userRepository.existsByUsername("hacker")).thenReturn(true);

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(user);
        });

        assertEquals("Username already exists", exception.getMessage());
        // check user wasnt saved
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_ShouldReturnUser_WhenIdExists() {
        // ARRANGE
        User user = new User();
        user.setUsername("maria");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // ACT
        User foundUser = userService.getUserById(1L);

        // ASSERT
        assertNotNull(foundUser);
        assertEquals("maria", foundUser.getUsername());
    }
}