import { Component, inject } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private router = inject(Router);

  loginData = {
    email: '',
    password: ''
  };

  onLogin(form: NgForm) {
    if (form.invalid) {
      return;
    }
    console.log('Submitted data:', this.loginData);

    this.router.navigate(['/questions']).then(success => {
      if (!success) {
        console.error('Navigation to /questions failed');
      }
    });
  }
}
