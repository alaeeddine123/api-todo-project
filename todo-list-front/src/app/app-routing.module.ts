import { NgModule } from '@angular/core';
import { RouterModule, Routes, CanActivateFn, Router } from '@angular/router';
import { AuthGuard } from './auth/auth.guard';
import { inject } from '@angular/core';
import { KeycloakService } from './services/keycloak/keycloak.service';
import { map, take } from 'rxjs';

const authCheck: CanActivateFn = () => {
  const keycloakService = inject(KeycloakService);
  const router = inject(Router);

  return keycloakService.isAuthenticated$.pipe(
    take(1),
    map(isAuthenticated => {
      if (isAuthenticated) {
        return true; // Allow navigation
      } else {
        router.navigate(['/auth/login']);
        return false;
      }
    })
  );
};

const routes: Routes = [
  {
    path: '',
    redirectTo: '/espace-user/dashboard', // Redirect to the protected area
    pathMatch: 'full'
  },
  {
    path: 'auth',
    loadChildren: () => import('./auth/auth.module').then(m => m.AuthModule)
  },
  {
    path: 'espace-user',
    canActivate: [authCheck], // Use CanActivate to protect the module
    loadChildren: () => import('./espace-user/espace-user.module').then(m => m.EspaceUserModule)
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    initialNavigation: 'enabledBlocking',
    useHash: false,
  })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
