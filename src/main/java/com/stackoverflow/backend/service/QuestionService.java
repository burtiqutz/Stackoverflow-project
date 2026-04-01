package com.stackoverflow.backend.service;

import com.stackoverflow.backend.entity.Question;
import com.stackoverflow.backend.entity.QuestionStatus;
import com.stackoverflow.backend.entity.User;
import com.stackoverflow.backend.repository.QuestionRepository;
import com.stackoverflow.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    private void validateQuestion(Question question) {
        if (question.getTitle() == null || question.getTitle().trim().isEmpty()) {
            throw new RuntimeException("Title is required!");
        }

        if (question.getText() == null || question.getText().trim().isEmpty()) {
            throw new RuntimeException("Text is required!");
        }

        if (question.getTags() == null || question.getTags().isEmpty()) {
            throw new RuntimeException("Question must have at least one tag!");
        }
    }

    public Question createQuestion(Question question, Long authorId) {
        validateQuestion(question);

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        question.setAuthor(author);
        question.setCreatedAt(LocalDateTime.now());
        question.setStatus(QuestionStatus.RECEIVED);

        return questionRepository.save(question);
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAllByOrderByCreatedAtDesc();
    }

    public Question getQuestionById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found!"));
    }

    public Question updateQuestion(Question question, Long id, Long authorId) {
        validateQuestion(question);

        Question updatedQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found!"));

        if (!updatedQuestion.getAuthor().getId().equals(authorId)) {
            throw new RuntimeException("You can update only your own question!");
        }

        updatedQuestion.setTitle(question.getTitle());
        updatedQuestion.setText(question.getText());
        updatedQuestion.setImage(question.getImage());
        updatedQuestion.setTags(question.getTags());

        return questionRepository.save(updatedQuestion);
    }

    public void deleteQuestion(Long id, Long authorId) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found!"));

        if (!question.getAuthor().getId().equals(authorId)) {
            throw new RuntimeException("You can delete only your own question!");
        }

        questionRepository.delete(question);
    }

    public List<Question> searchQuestionsByTitle(String title) {
        return questionRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(title);
    }

    public List<Question> getQuestionsByTag(String tag) {
        return questionRepository.findByTagsContainingOrderByCreatedAtDesc(tag);
    }

    public List<Question> getQuestionsByAuthor(Long authorId) {
        return questionRepository.findByAuthorIdOrderByCreatedAtDesc(authorId);
    }

    public List<Question> getMyQuestions(Long authorId) {
        return questionRepository.findByAuthorIdOrderByCreatedAtDesc(authorId);
    }
}