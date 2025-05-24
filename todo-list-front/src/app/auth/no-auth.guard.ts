import {Injectable} from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { KeycloakService } from '../services/keycloak/keycloak.service';
import { Observable, take, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NoAuthGuard implements CanActivate {
  constructor(
    private keycloakService: KeycloakService,
    private router: Router
  ) {}

  canActivate(): Observable<boolean> {
    return this.keycloakService.isAuthenticated$.pipe(
      take(1),
      map(isAuthenticated => {
        if (isAuthenticated) {
          console.log('NoAuthGuard: User is authenticated, redirecting to dashboard');
          this.router.navigate(['/espace-user/dashboard']);
          return false;
        }
        console.log('NoAuthGuard: User is not authenticated, allowing access to login');
        return true;
      })
    );
  }
}
