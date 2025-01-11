  // src/app/auth/auth.interceptor.ts
  import { Injectable } from '@angular/core';
  import {
    HttpRequest,
    HttpHandler,
    HttpEvent,
    HttpInterceptor,
    HttpErrorResponse,
    HttpResponse
  } from '@angular/common/http';
  import { Observable, throwError } from 'rxjs';
  import { catchError, tap } from 'rxjs/operators';
  import { TokenService } from '../services/token/token.service';
  import { Router } from '@angular/router';

  @Injectable()
  export class AuthInterceptor implements HttpInterceptor {
    constructor(
      private tokenService: TokenService,
      private router: Router
    ) {}


/*  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let modifiedRequest = request.clone({
      withCredentials: true
    });

    return next.handle(modifiedRequest).pipe(
      tap(event => {
        if (event instanceof HttpResponse) {
          console.group('Response Interceptor');
          console.log('All Headers:',
            Array.from(event.headers.keys()).map(key =>
              `${key}: ${event.headers.get(key)}`
            )
          );

          // Check for Set-Cookie header
          const setCookieHeader = event.headers.get('Set-Cookie');
          console.log('Set-Cookie Header:', setCookieHeader);

          // Check for X-Token headers
          const xTokenHeader = event.headers.get('X-Token-Set');
          console.log('X-Token-Set Header:', xTokenHeader);
          console.groupEnd();
        }
      }),
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401 || error.status === 403) {
          this.router.navigate(['/auth/login']);
        }
        return throwError(() => error);
      })
    );
  }*/
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Ensure withCredentials is set for all requests
    let modifiedRequest = request.clone({
      withCredentials: true,
    });

    // Skip token for specific endpoints
    const skipTokenEndpoints = [
      '/auth/authenticate',
      '/auth/register',
      '/auth/logout',
      '/verify-session'
    ];

    if (!skipTokenEndpoints.some(endpoint => request.url.includes(endpoint))) {
      const token = this.tokenService.getToken();
      console.log('Interceptor - Token:', token);

      if (token) {
        modifiedRequest = modifiedRequest.clone({
          headers: modifiedRequest.headers.set('Authorization', `Bearer ${token}`)
        });
      }
    }

    return next.handle(modifiedRequest).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401 || error.status === 403) {
          console.log('Unauthorized Access - Redirecting to Login');
          this.router.navigate(['/auth/login']);
        }
        return throwError(() => error);
      })
    );
  }
}

