import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { Register } from './pages/register/register';
import { UserProfile } from './pages/user-profile/user-profile';
import { QuestionsList } from './pages/questions-list/questions-list';
import { QuestionDetail } from './pages/question-detail/question-detail';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: 'profile/:id', component: UserProfile },
  { path: 'questions', component: QuestionsList },
  { path: 'questions/:id', component: QuestionDetail },
  { path: '', redirectTo: '/questions', pathMatch: 'full' }
];
