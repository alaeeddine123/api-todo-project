import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, map, of, tap, throwError } from 'rxjs';

const baseUrl = 'http://localhost:8088/api/v1/auth';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  constructor(private http: HttpClient) {}


  getToken(): string | null {
    try {
      console.group('Comprehensive Token Retrieval');

      const localStorageToken = localStorage.getItem('jwt');
      if (localStorageToken) {
        console.groupEnd();
        return localStorageToken;
      }

      const cookies = document.cookie.split('; ');

      const jwtCookie = cookies.find(cookie => cookie.startsWith('jwt='));

      if (jwtCookie) {
        const token = jwtCookie.split('=')[1];
        console.groupEnd();
        return token;
      }

      console.groupEnd();
      return null;
    } catch (error) {
      console.error('Error retrieving token:', error);
      console.groupEnd();
      return null;
    }
  }


  saveToken(token: string): void {
    try {
      // Save to localStorage first
      localStorage.setItem('jwt', token);

      // Set cookie with more explicit parameters
      document.cookie = `jwt=${token}; path=/; domain=localhost; max-age=3600; SameSite=Lax; Secure=false`;

      // Optional: Use backend endpoint to set cookie
      this.http.post<void>(`${baseUrl}/set-cookie`,
        { token: token },
        { withCredentials: true }
      ).pipe(
        catchError(error => {
          console.error('Backend cookie set failed', error);
          return of(null);
        })
      ).subscribe();

    } catch (error) {
      console.error('Error saving token:', error);
    }
  }


  logout(): Observable<any> {
    return this.http.post(`${baseUrl}/logout`, {}, {
      withCredentials: true
    });
  }
}
