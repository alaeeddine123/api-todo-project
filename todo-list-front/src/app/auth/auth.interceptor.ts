import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { KeycloakService } from '../services/keycloak/keycloak.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private keycloakService: KeycloakService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Skip Keycloak URLs
    if (request.url.startsWith(this.keycloakService.authServerUrl!)) {
      return next.handle(request);
    }

    // Add token to API requests
    const token = this.keycloakService.getToken();
    console.log('🔍 Interceptor Debug:');
    console.log('- Token available:', !!token);
    console.log('- Token (first 50 chars):', token?.substring(0, 50));
    console.log('- Request URL:', request.url);

    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });
      console.log('✅ Added Authorization header');
    } else {
      console.warn('❌ No token available');
    }

    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('🚨 HTTP Error:', {
          status: error.status,
          statusText: error.statusText,
          url: error.url,
          message: error.message
        });

        if (error.status === 401) {
          console.log('🔄 401 Unauthorized - redirecting to login');
          this.keycloakService.login();
        }

        return throwError(() => error);
      })
    );
  }
}
