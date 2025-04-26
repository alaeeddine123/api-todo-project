import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { KeycloakService } from '../services/keycloak/keycloak.service';
import { Observable, tap, map, take } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private keycloakService: KeycloakService, private router: Router) {}

  canActivate(): Observable<boolean> {
    console.log('AuthGuard.canActivate - checking authentication...');

    return this.keycloakService.isAuthenticated$.pipe(
      take(1),
      tap(isAuthenticated => console.log('AuthGuard.canActivate - current authentication state:', isAuthenticated)),
      map(isAuthenticated => {
        if (isAuthenticated) {
          return true;
        } else {
          console.log('AuthGuard.canActivate - not authenticated, redirecting to login');
          this.keycloakService.login();
          return false;
        }
      })
    );
  }
}
