package com.stackoverflow.backend.service;

import com.stackoverflow.backend.entity.Question;
import com.stackoverflow.backend.entity.QuestionStatus;
import com.stackoverflow.backend.entity.User;
import com.stackoverflow.backend.repository.QuestionRepository;
import com.stackoverflow.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private QuestionService questionService;

    @Test
    void createQuestion_ShouldSaveQuestion_WhenDataIsValid() {
        User author = new User();
        author.setId(1L);
        author.setUsername("delia");

        Question question = new Question();
        question.setTitle("Cum merge JPA?");
        question.setText("Am o problema la mapare.");

        when(userRepository.findById(1L)).thenReturn(Optional.of(author));
        when(questionRepository.save(any(Question.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Question savedQuestion = questionService.createQuestion(question, 1L);

        assertNotNull(savedQuestion);
        assertEquals("Cum merge JPA?", savedQuestion.getTitle());
        assertEquals(author, savedQuestion.getAuthor());
        assertEquals(QuestionStatus.RECEIVED, savedQuestion.getStatus());
        assertNotNull(savedQuestion.getCreatedAt());

        verify(questionRepository, times(1)).save(question);
    }

    @Test
    void createQuestion_ShouldThrowException_WhenAuthorDoesNotExist() {
        Question question = new Question();
        question.setTitle("Titlu");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            questionService.createQuestion(question, 1L);
        });

        assertEquals("User not found!", exception.getMessage());
        verify(questionRepository, never()).save(any(Question.class));
    }

    @Test
    void getQuestionById_ShouldReturnQuestion_WhenIdExists() {
        Question question = new Question();
        question.setId(1L);
        question.setTitle("Titlu test");

        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        Question foundQuestion = questionService.getQuestionById(1L);

        assertNotNull(foundQuestion);
        assertEquals("Titlu test", foundQuestion.getTitle());
    }

    @Test
    void getAllQuestions_ShouldReturnListOfQuestions() {
        Question q1 = new Question();
        q1.setTitle("Q1");

        Question q2 = new Question();
        q2.setTitle("Q2");

        when(questionRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(q1, q2));

        List<Question> questions = questionService.getAllQuestions();

        assertEquals(2, questions.size());
        verify(questionRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void updateQuestion_ShouldUpdateQuestion_WhenAuthorMatches() {
        User author = new User();
        author.setId(1L);

        Question existingQuestion = new Question();
        existingQuestion.setId(1L);
        existingQuestion.setAuthor(author);
        existingQuestion.setTitle("Titlu vechi");
        existingQuestion.setText("Text vechi");

        Question newQuestionData = new Question();
        newQuestionData.setTitle("Titlu nou");
        newQuestionData.setText("Text nou");
        newQuestionData.setImage("nou.png");
        newQuestionData.setTags(List.of("java", "spring"));

        when(questionRepository.findById(1L)).thenReturn(Optional.of(existingQuestion));
        when(questionRepository.save(any(Question.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Question updatedQuestion = questionService.updateQuestion(newQuestionData, 1L, 1L);

        assertEquals("Titlu nou", updatedQuestion.getTitle());
        assertEquals("Text nou", updatedQuestion.getText());
        assertEquals("nou.png", updatedQuestion.getImage());
        assertEquals(2, updatedQuestion.getTags().size());
    }

    @Test
    void updateQuestion_ShouldThrowException_WhenAuthorDoesNotMatch() {
        User author = new User();
        author.setId(1L);

        Question existingQuestion = new Question();
        existingQuestion.setId(1L);
        existingQuestion.setAuthor(author);

        Question newQuestionData = new Question();
        newQuestionData.setTitle("Alt titlu");

        when(questionRepository.findById(1L)).thenReturn(Optional.of(existingQuestion));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            questionService.updateQuestion(newQuestionData, 1L, 2L);
        });

        assertEquals("You can update only your own question!", exception.getMessage());
        verify(questionRepository, never()).save(any(Question.class));
    }

    @Test
    void deleteQuestion_ShouldDeleteQuestion_WhenAuthorMatches() {
        User author = new User();
        author.setId(1L);

        Question question = new Question();
        question.setId(1L);
        question.setAuthor(author);

        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        questionService.deleteQuestion(1L, 1L);

        verify(questionRepository, times(1)).delete(question);
    }

    @Test
    void deleteQuestion_ShouldThrowException_WhenAuthorDoesNotMatch() {
        User author = new User();
        author.setId(1L);

        Question question = new Question();
        question.setId(1L);
        question.setAuthor(author);

        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            questionService.deleteQuestion(1L, 2L);
        });

        assertEquals("You can delete only your own question!", exception.getMessage());
        verify(questionRepository, never()).delete(any(Question.class));
    }
}