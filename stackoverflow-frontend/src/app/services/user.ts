import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserProfileData } from '../pages/user-profile/user-profile';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private http = inject(HttpClient);

  private apiUrl = 'http://localhost:8080/api/users';

  getUserById(id: number): Observable<UserProfileData> {
    return this.http.get<UserProfileData>(`${this.apiUrl}/${id}`);
  }

  registerUser(userData: any): Observable<any> {
    return this.http.post(this.apiUrl, userData);
  }

  loginUser(credentials: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, credentials);
  }
}
