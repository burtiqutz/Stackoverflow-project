package com.stackoverflow.backend.controller;

import com.stackoverflow.backend.entity.Answer;
import com.stackoverflow.backend.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping
    public Answer save(@RequestBody Answer answer) {
        return answerService.createAnswer(answer);
    }

    @GetMapping
    public List<Answer> findAll() {
        return answerService.getAllAnswers();
    }

    @GetMapping("/{id}")
    public Answer findById(@PathVariable Long id) {
        return answerService.getAnswerById(id);
    }

    @GetMapping("/question/{questionId}")
    public List<Answer> findByQuestionId(@PathVariable Long questionId) {
        return answerService.getAnswersByQuestionId(questionId);
    }

    @PutMapping("/{id}")
    public Answer update(@RequestBody Answer answer, @PathVariable Long id) {
        return answerService.updateAnswer(answer, id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        answerService.deleteAnswer(id);
    }

}