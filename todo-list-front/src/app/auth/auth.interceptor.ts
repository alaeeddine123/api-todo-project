  import { Injectable } from '@angular/core';
  import {
    HttpRequest,
    HttpHandler,
    HttpEvent,
    HttpInterceptor,
    HttpErrorResponse,
  } from '@angular/common/http';
  import { Observable, throwError } from 'rxjs';
  import { catchError, tap } from 'rxjs/operators';
  import { Router } from '@angular/router';
import { AuthenticationService } from './auth.service';

  @Injectable()
  export class AuthInterceptor implements HttpInterceptor {
    constructor(
      private authService: AuthenticationService,
      private router: Router
    ) {}


  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const skipTokenEndpoints = [
      '/auth/authenticate',
      '/auth/register',
      '/auth/logout',
      '/verify-session'
    ];

    if (!skipTokenEndpoints.some(endpoint => request.url.includes(endpoint))) {
      const token = this.authService.getToken();
      if (token) {
        request = request.clone({
          headers: request.headers.set('Authorization', `Bearer ${token}`)
        });
      }
    }

    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401 || error.status === 403) {
          this.router.navigate(['/auth/login']);
        }
        return throwError(() => error);
      })
    );
  }
}

