package com.stackoverflow.backend.service;

import com.stackoverflow.backend.entity.Answer;
import com.stackoverflow.backend.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    public Answer createAnswer(Answer answer) {
        return answerRepository.save(answer);
    }

    public List<Answer> getAllAnswers() {
        return answerRepository.findAll();
    }

    public Answer getAnswerById(Long id) {
        return answerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Answer not found!"));
    }

    public List<Answer> getAnswersByQuestionId(Long questionId) {
        return answerRepository.findByQuestionId(questionId);
    }


    public Answer updateAnswer(Answer answer, Long id) {
        Answer updatedAnswer = answerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Answer not found!"));

        updatedAnswer.setText(answer.getText());
        updatedAnswer.setImagine(answer.getImagine());

        return answerRepository.save(updatedAnswer);
    }

    public void deleteAnswer(Long id) {
        answerRepository.deleteById(id);
    }
}