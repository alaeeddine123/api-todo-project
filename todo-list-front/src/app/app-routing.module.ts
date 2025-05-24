import { NgModule } from '@angular/core';
import { RouterModule, Routes, CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { KeycloakService } from './services/keycloak/keycloak.service';
import { map, take } from 'rxjs';

const authCheck: CanActivateFn = () => {
  const keycloakService = inject(KeycloakService);

  return keycloakService.isAuthenticated$.pipe(
    take(1),
    map(isAuthenticated => {
      if (!isAuthenticated) {
        keycloakService.login();
      }
      return isAuthenticated;
    })
  );
};

const routes: Routes = [
  {
    path: '',
    redirectTo: '/espace-user/dashboard',
    pathMatch: 'full'
  },
  {
    path: 'espace-user',
    canActivate: [authCheck],
    loadChildren: () => import('./espace-user/espace-user.module').then(m => m.EspaceUserModule)
  },
  {
    path: '**',
    redirectTo: '/espace-user/dashboard'
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
