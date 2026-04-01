package com.stackoverflow.backend.controller;

import com.stackoverflow.backend.entity.Question;
import com.stackoverflow.backend.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    // create
    @PostMapping("/author/{authorId}")
    public Question save(@RequestBody Question question, @PathVariable Long authorId) {
        return questionService.createQuestion(question, authorId);
    }

    // read all
    @GetMapping
    public List<Question> findAll() {
        return questionService.getAllQuestions();
    }

    // read by id
    @GetMapping("/{id}")
    public Question findById(@PathVariable Long id) {
        return questionService.getQuestionById(id);
    }

    // update
    @PutMapping("/{id}/author/{authorId}")
    public Question update(@RequestBody Question question,
                           @PathVariable Long id,
                           @PathVariable Long authorId) {
        return questionService.updateQuestion(question, id, authorId);
    }

    // delete
    @DeleteMapping("/{id}/author/{authorId}")
    public void deleteById(@PathVariable Long id, @PathVariable Long authorId) {
        questionService.deleteQuestion(id, authorId);
    }

    // search by title
    @GetMapping("/search")
    public List<Question> searchByTitle(@RequestParam String title) {
        return questionService.searchQuestionsByTitle(title);
    }

    // filter by tag
    @GetMapping("/tag/{tag}")
    public List<Question> findByTag(@PathVariable String tag) {
        return questionService.getQuestionsByTag(tag);
    }

    // filter by author
    @GetMapping("/author-filter/{authorId}")
    public List<Question> findByAuthor(@PathVariable Long authorId) {
        return questionService.getQuestionsByAuthor(authorId);
    }

    // my questions
    @GetMapping("/my/{authorId}")
    public List<Question> findMyQuestions(@PathVariable Long authorId) {
        return questionService.getMyQuestions(authorId);
    }
}