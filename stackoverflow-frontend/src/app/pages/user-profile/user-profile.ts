import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user';

export interface UserProfileData {
  id: number;
  username: string;
  email: string;
  score: number;
  aboutMe?: string;
  memberSince?: string;
}

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-profile.html',
  styleUrl: './user-profile.css',
})
export class UserProfile implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private userService = inject(UserService);

  private cdr = inject(ChangeDetectorRef);

  currentUser!: UserProfileData;

  ngOnInit() {
    const userId = Number(this.route.snapshot.paramMap.get('id'));

    if (userId) {
      this.userService.getUserById(userId).subscribe({
        next: (data) => {
          console.log('Datele au venit:', data);
          this.currentUser = data;

          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error(err);
          this.router.navigate(['/']);
        }
      });
    }
  }

  onLogout() {
    this.router.navigate(['/login']).then((success) => {
      if (!success) {
        console.error('Navigation to /login failed');
      }
    });
  }
}
