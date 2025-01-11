import { HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject, of, switchMap } from 'rxjs';
import { map, catchError, throwError, tap, take, finalize } from 'rxjs';
import { AuthenticationRequest, AuthenticationReponse, RegisterRequest } from '../services/models';
import { TokenService } from '../services/token/token.service';
import { Router } from '@angular/router';

const baseUrl = 'http://localhost:8088/api/v1/auth';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private authStateSubject = new BehaviorSubject<boolean>(false);
  authState$ = this.authStateSubject.asObservable();
  private checkingAuth = false;

  constructor(
    private http: HttpClient,
    private tokenService: TokenService,
    private router: Router
  ) {
      // Check if there's a stored authentication state
  const storedAuthState = localStorage.getItem('isAuthenticated') === 'true';
  this.authStateSubject = new BehaviorSubject<boolean>(storedAuthState);
  this.authState$ = this.authStateSubject.asObservable();

    this.checkAuthStatus();
  }

  private checkAuthStatus(): void {
    if (this.checkingAuth) {
      return;
    }

    this.checkingAuth = true;
    this.verifySession().pipe(
      take(1),
      finalize(() => {
        this.checkingAuth = false;
      })
    ).subscribe({
      next: (isValid) => {
        console.log('Initial auth status:', isValid);

        // Explicitly set the auth state and persist it
        if (isValid) {
          this.authStateSubject.next(true);
          localStorage.setItem('isAuthenticated', 'true');
        } else {
          this.authStateSubject.next(false);
          localStorage.removeItem('isAuthenticated');
        }

        // Redirect logic
        if (!isValid && !window.location.pathname.includes('/auth/login')) {
          this.router.navigate(['/auth/login']);
        }
      },
      error: () => {
        this.authStateSubject.next(false);
        localStorage.removeItem('isAuthenticated');

        if (!window.location.pathname.includes('/auth/login')) {
          this.router.navigate(['/auth/login']);
        }
      }
    });
  }

  authenticate(request: AuthenticationRequest): Observable<AuthenticationReponse> {
    return this.http.post<AuthenticationReponse>(`${baseUrl}/authenticate`,
      request,
      {
        observe: 'response',
        withCredentials: true,
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        })
      }
    ).pipe(
      tap(response => {
        if (response.body && response.body.token) {
          // Save token to localStorage
          localStorage.setItem('jwt', response.body.token);

          // Set authentication state
          this.authStateSubject.next(true);
          localStorage.setItem('isAuthenticated', 'true');

          // Navigate
          this.router.navigate(['/espace-user/dashboard']);
        }
      }),
      map(response => response.body!),
      catchError(error => {
        // Clear authentication state on error
        this.authStateSubject.next(false);
        localStorage.removeItem('isAuthenticated');
        localStorage.removeItem('jwt');

        this.router.navigate(['/auth/login']);
        return throwError(() => error);
      })
    );
  }

  register(registerData: RegisterRequest): Observable<void> {
    return this.http.post<void>(
      `${baseUrl}/register`,
      registerData,
      { withCredentials: true }
    );
  }

  isAuthenticated(): Observable<boolean> {
    if (this.checkingAuth) {
      return this.authState$.pipe(take(1));
    }
    return this.verifySession().pipe(take(1));
  }

  private verifySession(): Observable<boolean> {
    console.log('===== Verify Session Start =====');
    const token = this.tokenService.getToken();
    console.log('Token retrieved:', token);

    if (!token) {
      console.log('No token found');
      this.authStateSubject.next(false);
      return of(false);
    }

    return this.http.get<void>(`${baseUrl}/verify-session`, {
      observe: 'response',
      withCredentials: true,
      headers: new HttpHeaders({
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      })
    }).pipe(
      map(response => {
        console.log('Verify session successful');
        this.authStateSubject.next(true);
        return true;
      }),
      catchError((error: HttpErrorResponse) => {
        console.error('Verify session failed', error);
        // Remove invalid token
        localStorage.removeItem('jwt');
        document.cookie = 'jwt=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';

        this.authStateSubject.next(false);
        return of(false);
      })
    );
  }

  logout(): Observable<any> {
    return this.http.post(`${baseUrl}/logout`, {}, {
      withCredentials: true
    }).pipe(
      tap(() => {
        // Clear authentication state
        this.authStateSubject.next(false);

        // Remove stored tokens and auth state
        localStorage.removeItem('jwt');
        localStorage.removeItem('isAuthenticated');

        // Clear the token cookie
        document.cookie = 'jwt=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; domain=localhost;';

        // Navigate to login page
        this.router.navigate(['/auth/login']);
      }),
      catchError(error => {
        // Even if logout fails, clear local state
        this.authStateSubject.next(false);
        localStorage.removeItem('jwt');
        localStorage.removeItem('isAuthenticated');

        // Clear the token cookie
        document.cookie = 'jwt=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; domain=localhost;';

        this.router.navigate(['/auth/login']);
        return throwError(() => error);
      })
    );
  }

  private handleError(error: HttpErrorResponse) {
    console.error('Authentication Error:', error);
    this.authStateSubject.next(false);

    if (error.status === 401 || error.status === 403) {
      if (!window.location.pathname.includes('/auth/login')) {
        this.router.navigate(['/auth/login']);
      }
      return throwError(() => ({
        error: { errorMsg: 'Invalid credentials' }
      }));
    }

    return throwError(() => ({
      error: { errorMsg: 'An unexpected error occurred' }
    }));
  }
}
