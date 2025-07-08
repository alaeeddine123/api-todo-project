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
  {
    path: '',
    redirectTo: '/app/dashboard',
    pathMatch: 'full'
  },
  {
    path: 'app',
    component: MainLayoutComponent,
    canActivate: [authCheck],
    children: [
      {
        path: 'dashboard',
        loadChildren: () => import('./espace-user/espace-user.module').then(m => m.EspaceUserModule),
        data: {
          breadcrumb: [
            { label: 'Dashboard', icon: 'dashboard' }
          ]
        }
      },
      {
        path: 'targets',
        loadChildren: () => import('./targets/targets.module').then(m => m.TargetsModule),
        data: {
          breadcrumb: [
            { label: 'Dashboard', route: '/app/dashboard', icon: 'dashboard' },
            { label: 'Target Companies' }
          ]
        }
      }
    ]
  },
  // ... rest of your routes
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    initialNavigation: 'enabledBlocking',
    useHash: false,
  })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
