import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject, of } from 'rxjs';
import { map, catchError, throwError, tap, take, finalize } from 'rxjs';
import { AuthenticationRequest, AuthenticationReponse, RegisterRequest } from '../services/models';
import { Router } from '@angular/router';

const baseUrl = 'http://localhost:8088/api/v1/auth';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private authStateSubject = new BehaviorSubject<boolean>(false);
  private tokenSubject = new BehaviorSubject<string | null>(null);

  token$ = this.tokenSubject.asObservable();
  authState$ = this.authStateSubject.asObservable();
  private checkingAuth = false;

  constructor(
    private http: HttpClient,
    private router: Router
  ) {

    this.checkAuthStatus();
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
          this.tokenSubject.next(response.body.token);
          this.authStateSubject.next(true);

          // Navigate
          this.router.navigate(['/espace-user/dashboard']);
        }
      }),
      map(response => response.body!),
      catchError(error => {
        // Clear authentication state on error
        this.tokenSubject.next(null);
        this.authStateSubject.next(false);

        this.router.navigate(['/auth/login']);
        return throwError(() => error);
      })
    );
  }

  private checkAuthStatus(): void {
    if (this.checkingAuth) return;

    this.checkingAuth = true;
    this.verifySession().pipe(
      take(1),
      finalize(() => {
        this.checkingAuth = false;
      })
    ).subscribe({
      next: (isValid) => {
        this.authStateSubject.next(isValid);
        if (!isValid && !window.location.pathname.includes('/auth/login')) {
          this.router.navigate(['/auth/login']);
        }
      },
      error: () => {
        this.tokenSubject.next(null);
        this.authStateSubject.next(false);
        if (!window.location.pathname.includes('/auth/login')) {
          this.router.navigate(['/auth/login']);
        }
      }
    });
  }


  register(registerData: RegisterRequest): Observable<void> {
    return this.http.post<void>(
      `${baseUrl}/register`,
      registerData,
      { withCredentials: true }
    );
  }

  getToken(): string | null {
    return this.tokenSubject.value;
  }


  isAuthenticated(): Observable<boolean> {
    if (this.checkingAuth) {
      return this.authState$.pipe(take(1));
    }
    return this.verifySession().pipe(take(1));
  }

  private verifySession(): Observable<boolean> {
    const token = this.tokenSubject.value;

    if (!token) {
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
      map(() => true),
      catchError(() => {
        this.tokenSubject.next(null);
        return of(false);
      })
    );
  }

  logout(): Observable<any> {
    return this.http.post(`${baseUrl}/logout`, {}, {
      withCredentials: true
    }).pipe(
      tap(() => {
        this.tokenSubject.next(null);
        this.authStateSubject.next(false);
        this.router.navigate(['/auth/login']);
      }),
      catchError(error => {
        this.tokenSubject.next(null);
        this.authStateSubject.next(false);
        this.router.navigate(['/auth/login']);
        return throwError(() => error);
      })
    );
  }
}
