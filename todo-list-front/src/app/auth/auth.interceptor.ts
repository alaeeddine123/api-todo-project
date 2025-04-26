import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
} from '@angular/common/http';
import { Observable, throwError, from } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { KeycloakService } from '../services/keycloak/keycloak.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private keycloakService: KeycloakService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (request.url.startsWith(this.keycloakService.authServerUrl!)) {
      return next.handle(request);
    }

    return from(this.handleIntercept(request, next));
  }

  private async handleIntercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Promise<HttpEvent<any>> {
    try {
      const token = await this.keycloakService.instance.token;
      if (token) {
        request = request.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`,
          },
        });
      }
      return next.handle(request).pipe(
        catchError((error: HttpErrorResponse) => {
          if (error.status === 401 && !request.url.includes('/auth/')) {
            return from(this.keycloakService.instance.updateToken(5)).pipe(
              catchError(() => {
                this.keycloakService.login();
                return throwError(() => error);
              }),
              switchMap(() => from(this.handleIntercept(request, next)))
            );
          }
          return throwError(() => error);
        })
      ).toPromise() as Promise<HttpEvent<any>>;
    } catch (error) {
      console.error('Failed to get token:', error);
      return next.handle(request).toPromise() as Promise<HttpEvent<any>>;
    }
  }
}
