import { NgModule } from '@angular/core';
import { RouterModule, Routes, CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { KeycloakService } from './services/keycloak/keycloak.service';
import { map, take } from 'rxjs';
import { MainLayoutComponent } from './core/layout/main-layout/main-layout.component';

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
  // Root redirect
  {
    path: '',
    redirectTo: '/app/dashboard',
    pathMatch: 'full'
  },

  // Main app layout with all authenticated routes
  {
    path: 'app',  // Changed from '' to 'app' to avoid conflict
    component: MainLayoutComponent,
    canActivate: [authCheck],
    children: [
      {
        path: 'dashboard',
        loadChildren: () => import('./espace-user/espace-user.module').then(m => m.EspaceUserModule)
      },
      {
        path: 'targets',
        loadChildren: () => import('./targets/targets.module').then(m => m.TargetsModule)
      }
    ]
  },

  // Legacy redirects
  {
    path: 'espace-user',
    redirectTo: '/app/dashboard',
    pathMatch: 'full'
  },
  {
    path: 'espace-user/**',
    redirectTo: '/app/dashboard'
  },

  // Fallback
  {
    path: '**',
    redirectTo: '/app/dashboard'
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
