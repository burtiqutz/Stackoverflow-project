package com.stackoverflow.backend.repository;

import com.stackoverflow.backend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAllByOrderByCreatedAtDesc();

    List<Question> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String title);

    List<Question> findByTagsContainingOrderByCreatedAtDesc(String tag);

    List<Question> findByAuthorIdOrderByCreatedAtDesc(Long authorId);
}