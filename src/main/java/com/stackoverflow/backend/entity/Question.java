package com.stackoverflow.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "answers")
@Data
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
