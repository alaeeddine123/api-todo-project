import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject, of } from 'rxjs';
import { map, catchError, throwError, tap, take, finalize } from 'rxjs';
import { AuthenticationRequest } from '../services/models';
import { Router } from '@angular/router';

const baseUrl = 'http://localhost:8088/api/v1/auth';

interface TokenResponse {
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
}
interface AuthRequest {
  email: string;
  password: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private authStateSubject = new BehaviorSubject<boolean>(false);
  authState$ = this.authStateSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    // Check authentication status when service initializes
    this.checkAuthStatus();
  }

  // Modified to work with cookies
  authenticate(request: AuthenticationRequest): Observable<any> {
    console.log("got into authenticate service method front ")
    return this.http.post<any>(
      `${baseUrl}/authenticate`,
      request,
      { withCredentials: true } // This enables cookies
    ).pipe(
      tap(() => {
        // Update auth state
        this.authStateSubject.next(true);
        // Navigate to dashboard
        this.router.navigate(['/espace-user']);
      }),
      catchError(error => {
        console.error('Authentication error:', error);
        this.authStateSubject.next(false);
        return throwError(() => error);
      })
    );
  }

  // Check session validity
  private checkAuthStatus(): void {
    this.verifySession().pipe(take(1)).subscribe({
      next: (isValid) => {
        this.authStateSubject.next(isValid);
        if (!isValid && !window.location.pathname.includes('/auth/login')) {
          this.router.navigate(['/auth/login']);
        }
      },
      error: () => {
        this.authStateSubject.next(false);
        if (!window.location.pathname.includes('/auth/login')) {
          this.router.navigate(['/auth/login']);
        }
      }
    });
  }

  // Verify session with backend
  verifySession(): Observable<boolean> {
    return this.http.get<any>(`${baseUrl}/verify-session`, {
      withCredentials: true // Important for including cookies
    }).pipe(
      map(() => true), // If request succeeds, session is valid
      catchError(() => of(false)) // If request fails, session is invalid
    );
  }

  // Updated logout to work with cookies
  logout(): Observable<any> {
    return this.http.post(`${baseUrl}/logout`, {}, {
      withCredentials: true
    }).pipe(
      tap(() => {
        this.authStateSubject.next(false);
        this.router.navigate(['/auth/login']);
      }),
      catchError(error => {
        this.authStateSubject.next(false);
        this.router.navigate(['/auth/login']);
        return throwError(() => error);
      })
    );
  }

  // Simplified check
  isAuthenticated(): Observable<boolean> {
    return this.verifySession().pipe(take(1));
  }
}
