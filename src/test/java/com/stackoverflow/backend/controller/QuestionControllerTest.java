package com.stackoverflow.backend.controller;

import tools.jackson.databind.ObjectMapper;
import com.stackoverflow.backend.entity.Question;
import com.stackoverflow.backend.entity.QuestionStatus;
import com.stackoverflow.backend.entity.User;
import com.stackoverflow.backend.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuestionController.class)
class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuestionService questionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createQuestion_ShouldReturn200Ok_AndCreatedQuestion() throws Exception {

        User author = new User();
        author.setId(1L);
        author.setUsername("delia");

        Question questionRequest = new Question();
        questionRequest.setTitle("Cum folosesc Spring Boot?");
        questionRequest.setText("Am nevoie de ajutor la controllers.");
        questionRequest.setImage("img.png");
        questionRequest.setTags(List.of("spring", "java"));

        Question savedQuestion = new Question();
        savedQuestion.setId(1L);
        savedQuestion.setTitle("Cum folosesc Spring Boot?");
        savedQuestion.setText("Am nevoie de ajutor la controllers.");
        savedQuestion.setImage("img.png");
        savedQuestion.setTags(List.of("spring", "java"));
        savedQuestion.setCreatedAt(LocalDateTime.now());
        savedQuestion.setStatus(QuestionStatus.RECEIVED);
        savedQuestion.setAuthor(author);

        when(questionService.createQuestion(any(Question.class), eq(1L))).thenReturn(savedQuestion);

        mockMvc.perform(post("/api/questions/author/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Cum folosesc Spring Boot?"))
                .andExpect(jsonPath("$.status").value("RECEIVED"))
                .andExpect(jsonPath("$.author.id").value(1))
                .andExpect(jsonPath("$.author.username").value("delia"));
    }

    @Test
    void getAllQuestions_ShouldReturn200Ok_AndListOfQuestions() throws Exception {

        User author = new User();
        author.setId(1L);
        author.setUsername("delia");

        Question question = new Question();
        question.setId(1L);
        question.setTitle("Intrebare test");
        question.setText("Text test");
        question.setImage("test.png");
        question.setTags(List.of("java"));
        question.setCreatedAt(LocalDateTime.now());
        question.setStatus(QuestionStatus.RECEIVED);
        question.setAuthor(author);

        when(questionService.getAllQuestions()).thenReturn(List.of(question));

        mockMvc.perform(get("/api/questions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Intrebare test"))
                .andExpect(jsonPath("$[0].author.username").value("delia"));
    }

    @Test
    void getQuestionById_ShouldReturn200Ok_AndQuestion() throws Exception {

        User author = new User();
        author.setId(1L);
        author.setUsername("delia");

        Question question = new Question();
        question.setId(1L);
        question.setTitle("Titlu");
        question.setText("Text");
        question.setImage("img.png");
        question.setTags(List.of("spring"));
        question.setCreatedAt(LocalDateTime.now());
        question.setStatus(QuestionStatus.RECEIVED);
        question.setAuthor(author);

        when(questionService.getQuestionById(1L)).thenReturn(question);

        mockMvc.perform(get("/api/questions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Titlu"))
                .andExpect(jsonPath("$.author.username").value("delia"));
    }

    @Test
    void updateQuestion_ShouldReturn200Ok_AndUpdatedQuestion() throws Exception {

        User author = new User();
        author.setId(1L);
        author.setUsername("delia");

        Question questionRequest = new Question();
        questionRequest.setTitle("Titlu nou");
        questionRequest.setText("Text nou");
        questionRequest.setImage("nou.png");
        questionRequest.setTags(List.of("java", "spring"));

        Question updatedQuestion = new Question();
        updatedQuestion.setId(1L);
        updatedQuestion.setTitle("Titlu nou");
        updatedQuestion.setText("Text nou");
        updatedQuestion.setImage("nou.png");
        updatedQuestion.setTags(List.of("java", "spring"));
        updatedQuestion.setCreatedAt(LocalDateTime.now());
        updatedQuestion.setStatus(QuestionStatus.RECEIVED);
        updatedQuestion.setAuthor(author);

        when(questionService.updateQuestion(any(Question.class), eq(1L), eq(1L))).thenReturn(updatedQuestion);

        mockMvc.perform(put("/api/questions/1/author/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Titlu nou"))
                .andExpect(jsonPath("$.text").value("Text nou"));
    }

    @Test
    void deleteQuestion_ShouldReturn200Ok() throws Exception {
        mockMvc.perform(delete("/api/questions/1/author/1"))
                .andExpect(status().isOk());
    }
}