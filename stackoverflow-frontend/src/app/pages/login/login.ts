import { Component, inject } from '@angular/core';
import {FormsModule, NgForm} from '@angular/forms'; // Am scos NgForm dacă nu îl folosești ca parametru
import {Router, RouterLink} from '@angular/router';
import { UserService } from '../../services/user';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink], // Am scos CommonModule, folosim noul Control Flow (@if)
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private router = inject(Router);
  private userService = inject(UserService);

  loginData = {
    email: '',
    password: ''
  };

  errorMessage: string | null = null;

  onLogin(form: NgForm) {
    if (form.invalid) return;

    this.userService.loginUser(this.loginData).subscribe({
      next: (response) => {
        console.log('Login succes!', response);
        localStorage.setItem('currentUser', JSON.stringify(response));

        this.router.navigate(['/profile', response.id]);
      },
      error: (err) => {
      }
    });
  }
}
