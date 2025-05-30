import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { NoAuthGuard } from './no-auth.guard';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [NoAuthGuard],
    data: { title: 'Login' }
  },
  {
    path: 'register',
    component: RegisterComponent,
    canActivate: [NoAuthGuard],
    data: { title: 'Register' }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule { }
