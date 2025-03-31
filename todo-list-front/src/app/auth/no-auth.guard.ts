import {Injectable} from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { Observable, map } from 'rxjs';
import { AuthenticationService } from './auth.service';
import { KeycloakService } from '../services/keycloak/keycloak.service';

@Injectable({
  providedIn: 'root'
})
export class NoAuthGuard implements CanActivate {
  constructor(
    private keycloakService: KeycloakService,
    private router: Router
  ) {}

  canActivate(): boolean {
    if (this.keycloakService.isAuthenticated()) {
      this.router.navigate(['/espace-user/dashboard']);
      return false;
    }

    return true;
  }
}
