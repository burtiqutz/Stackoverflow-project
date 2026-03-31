package com.stackoverflow.backend.controller;

import tools.jackson.databind.ObjectMapper;
import com.stackoverflow.backend.entity.User;
import com.stackoverflow.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser_ShouldReturn200Ok_AndTheCreatedUser() throws Exception {

        User userRequest = new User();
        userRequest.setUsername("hacker_student");
        userRequest.setEmail("test@facultate.ro");
        userRequest.setPassword("superparola");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("hacker_student");
        savedUser.setEmail("test@facultate.ro");
        savedUser.setScore(0.0);

        when(userService.createUser(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("hacker_student"))
                .andExpect(jsonPath("$.score").value(0.0));
    }
}