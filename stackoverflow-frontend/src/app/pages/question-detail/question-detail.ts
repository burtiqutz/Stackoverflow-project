import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Question } from '../../models/question.model';
import { QuestionService } from '../../services/question.service';
import { Answer } from '../../models/answer.model';
import { AnswerService } from '../../services/answer.service';

@Component({
  selector: 'app-question-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './question-detail.html',
  styleUrl: './question-detail.css',
})
export class QuestionDetail implements OnInit {
  questionId!: number;
  question?: Question;
  answers: Answer[] = [];

  questionVotes = 0;
  answerVotes: { [answerId: number]: number } = {};

  isLoading = true;
  isLoadingAnswers = true;
  errorMessage = '';
  answersErrorMessage = '';

  newAnswerText = '';
  newAnswerPicture = '';

  editingAnswerId: number | null = null;
  editedAnswerText = '';
  editedAnswerPicture = '';

  private currentUserId = 1;

  constructor(
    private route: ActivatedRoute,
    private questionService: QuestionService,
    private answerService: AnswerService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const idFromRoute = this.route.snapshot.paramMap.get('id');

    if (!idFromRoute) {
      this.errorMessage = 'Question id is missing.';
      this.isLoading = false;
      this.isLoadingAnswers = false;
      this.cdr.detectChanges();
      return;
    }

    this.questionId = Number(idFromRoute);

    if (Number.isNaN(this.questionId)) {
      this.errorMessage = 'Invalid question id.';
      this.isLoading = false;
      this.isLoadingAnswers = false;
      this.cdr.detectChanges();
      return;
    }

    this.loadQuestion();
    this.loadAnswers();
  }

  private loadQuestion(): void {
    this.questionService.getQuestionById(this.questionId).subscribe({
      next: (data) => {
        this.question = data;
        this.isLoading = false;
        this.errorMessage = '';
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('ERROR LOADING QUESTION:', error);
        this.errorMessage = 'Could not load question.';
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  private loadAnswers(): void {
    this.answerService.getAnswersByQuestionId(this.questionId).subscribe({
      next: (data) => {
        this.answers = data;
        this.isLoadingAnswers = false;
        this.answersErrorMessage = '';
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('ERROR LOADING ANSWERS:', error);
        this.answersErrorMessage = 'Could not load answers.';
        this.isLoadingAnswers = false;
        this.cdr.detectChanges();
      }
    });
  }

  addAnswer(): void {
    const trimmedText = this.newAnswerText.trim();

    if (!trimmedText) {
      return;
    }

    const answerPayload = {
      text: trimmedText,
      imagine: this.newAnswerPicture.trim(),
      author: {
        id: this.currentUserId
      },
      question: {
        id: this.questionId
      }
    };

    this.answerService.createAnswer(answerPayload).subscribe({
      next: (createdAnswer) => {
        this.answers = [...this.answers, createdAnswer];

        this.newAnswerText = '';
        this.newAnswerPicture = '';
        this.answersErrorMessage = '';

        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('ERROR CREATING ANSWER:', error);
        this.answersErrorMessage = 'Could not create answer.';
        this.cdr.detectChanges();
      }
    });
  }

  startEdit(answer: Answer): void {
    this.editingAnswerId = answer.id;
    this.editedAnswerText = answer.text;
    this.editedAnswerPicture = answer.imagine || '';
    this.cdr.detectChanges();
  }

  cancelEdit(): void {
    this.editingAnswerId = null;
    this.editedAnswerText = '';
    this.editedAnswerPicture = '';
    this.cdr.detectChanges();
  }

  saveEdit(answerId: number): void {
    const trimmedText = this.editedAnswerText.trim();

    if (!trimmedText) {
      return;
    }

    const oldAnswer = this.answers.find((answer) => answer.id === answerId);

    if (!oldAnswer) {
      return;
    }

    const updatedPayload = {
      ...oldAnswer,
      text: trimmedText,
      imagine: this.editedAnswerPicture.trim()
    };

    this.answerService.updateAnswer(answerId, updatedPayload).subscribe({
      next: (updatedAnswer) => {
        this.answers = this.answers.map((answer) =>
          answer.id === answerId ? updatedAnswer : answer
        );

        this.answersErrorMessage = '';
        this.cancelEdit();
      },
      error: (error) => {
        console.error('ERROR UPDATING ANSWER:', error);
        this.answersErrorMessage = 'Could not update answer.';
        this.cdr.detectChanges();
      }
    });
  }

  deleteAnswer(answerId: number): void {
    this.answerService.deleteAnswer(answerId).subscribe({
      next: () => {
        this.answers = this.answers.filter((answer) => answer.id !== answerId);
        this.answersErrorMessage = '';
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('ERROR DELETING ANSWER:', error);
        this.answersErrorMessage = 'Could not delete answer.';
        this.cdr.detectChanges();
      }
    });
  }

  upvoteQuestion(): void {
    this.questionVotes++;
    this.cdr.detectChanges();
  }

  downvoteQuestion(): void {
    this.questionVotes--;
    this.cdr.detectChanges();
  }

  upvoteAnswer(answerId: number): void {
    this.answerVotes[answerId] = this.getAnswerVotes(answerId) + 1;
    this.cdr.detectChanges();
  }

  downvoteAnswer(answerId: number): void {
    this.answerVotes[answerId] = this.getAnswerVotes(answerId) - 1;
    this.cdr.detectChanges();
  }

  getAnswerVotes(answerId: number): number {
    return this.answerVotes[answerId] || 0;
  }

  getAnswerAuthorName(answer: Answer): string {
    return answer.author?.username || answer.author?.name || answer.author?.email || 'Unknown user';
  }

  getQuestionAuthorName(question: Question): string {
    return question.author?.username || question.author?.name || question.author?.email || 'Unknown user';
  }
}
