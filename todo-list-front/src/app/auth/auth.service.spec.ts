import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { map, catchError, throwError } from 'rxjs';
import { AuthenticationRequest, AuthenticationReponse, RegisterRequest } from '../services/models';

const baseUrl = 'http://localhost:8088/api/v1/auth';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private http: HttpClient) {}

  authenticate(request: AuthenticationRequest): Observable<AuthenticationReponse> {
    console.log("request:", request);
    return this.http.post<AuthenticationReponse>(
      `${baseUrl}/authenticate`,
      request,
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        }),
        withCredentials: true
      }
    ).pipe(
      map(response => {
        console.log('Authentication successful', response);
        // Handle server-side cookie, no need to store token manually
        return response;
      }),
      catchError(this.handleError)
    );
  }

  register(registerData: RegisterRequest): Observable<void> {
    console.log("register request", registerData);
    return this.http.post<void>(
      `${baseUrl}/register`,
      registerData,
      { withCredentials: true }
    );
  }

  logout(): Observable<void> {
    return this.http.post<void>(
      `${baseUrl}/logout`,
      {},
      { withCredentials: true }
    );
  }

  private handleError(error: HttpErrorResponse) {
    console.error('HTTP Error:', error);

    if (error.status === 401 || error.status === 403) {
      return throwError(() => ({
        error: { errorMsg: 'Invalid email or password' }
      }));
    }

    return throwError(() => ({
      error: { errorMsg: 'An unexpected error occurred' }
    }));
  }
}
