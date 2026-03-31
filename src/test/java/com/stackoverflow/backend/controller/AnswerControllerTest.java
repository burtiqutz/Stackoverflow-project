package com.stackoverflow.backend.controller;

import com.stackoverflow.backend.entity.Answer;
import com.stackoverflow.backend.entity.Question;
import com.stackoverflow.backend.service.AnswerService;
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


@WebMvcTest(AnswerController.class)
class AnswerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnswerService answerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAnswer_ShouldReturn200Ok_AndTheCreatedAnswer() throws Exception {

        User author = new User();
        author.setId(1L);

        Question question = new Question();
        question.setId(10L);

        Answer answerRequest = new Answer();
        answerRequest.setText("Chat, am I cooked?");
        answerRequest.setImagine("screenshot.png");
        answerRequest.setQuestion(question);
        answerRequest.setAuthor(author);

        Answer savedAnswer = new Answer();
        savedAnswer.setId(100L);
        savedAnswer.setText("Chat, am I cooked?");
        savedAnswer.setImagine("screenshot.png");
        savedAnswer.setQuestion(question);
        savedAnswer.setAuthor(author);

        when(answerService.createAnswer(any(Answer.class))).thenReturn(savedAnswer);

        mockMvc.perform(post("/api/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(answerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.text").value("Chat, am I cooked?"))
                .andExpect(jsonPath("$.imagine").value("screenshot.png"))
                .andExpect(jsonPath("$.question.id").value(10))
                .andExpect(jsonPath("$.author.id").value(1));
    }
}