import { Component, inject } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import {UserService} from '../../services/user';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  private router = inject(Router);
  private userService = inject(UserService);

  registerData = {
    username: '',
    email: '',
    password: '',
    confirmPassword: ''
  };
  errorMessage: string | null = null;

  onRegister(form: NgForm) {
    if (form.invalid) {
      return;
    }

    if (this.registerData.password !== this.registerData.confirmPassword) {
      console.warn('Passwords don\'t match!');
      return;
    }
    const payload = {
      username: this.registerData.username,
      email: this.registerData.email,
      password: this.registerData.password
    };

    console.log('Sending data to backend:', payload);

    this.userService.registerUser(payload).subscribe({
      next: (response) => {
        console.log('Account successfully created', response);

        this.router.navigate(['/login']).then(success => {
          if (!success) {
            console.error('Navigation to /login failed');
          }
        });
      },
      error: (err) => {
        console.error('Error on register:', err);
        if (err.error && typeof err.error === 'string') {
          this.errorMessage = err.error;
        } else if (err.error && err.error.message) {
          this.errorMessage = err.error.message;
        } else {
          this.errorMessage = 'Server error, please try again later.';
        }
      }
    });
  }
}
