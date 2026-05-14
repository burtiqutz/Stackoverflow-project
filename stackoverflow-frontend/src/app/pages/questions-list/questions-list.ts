import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { QuestionService } from '../../services/question.service';
import { Question } from '../../models/question.model';

@Component({
  selector: 'app-questions-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './questions-list.html',
  styleUrl: './questions-list.css'
})
export class QuestionsList implements OnInit {
  questions: Question[] = [];
  isLoading = true;
  errorMessage = '';

  constructor(
    private questionService: QuestionService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.questionService.getQuestions().subscribe({
      next: (data) => {
        console.log('QUESTIONS FROM BACKEND:', data);
        this.questions = data;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('ERROR LOADING QUESTIONS:', error);
        this.errorMessage = 'Could not load questions.';
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  getQuestionAuthorName(question: Question): string {
    return question.author?.username || question.author?.name || question.author?.email || 'Unknown user';
  }
}
