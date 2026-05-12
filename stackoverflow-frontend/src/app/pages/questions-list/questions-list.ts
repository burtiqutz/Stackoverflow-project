import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { QuestionService } from '../../services/question.service';
import { Question } from '../../models/question.model';

@Component({
  selector: 'app-questions-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './questions-list.html',
  styleUrl: './questions-list.css'
})

export class QuestionsList implements OnInit {
  questions: Question[] = [];

  constructor(private questionService: QuestionService) {
  }

  ngOnInit(): void {
    this.questionService.getQuestions().subscribe(data => {
      //console.log('QUESTIONS FROM JSON:', data);
      this.questions = data;
    });
  }
}
