import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { KeycloakService } from './../../services/keycloak/keycloak.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  errorMessage: string[] = [];
  isLoading = false;
  registerFlag = false;
  userProfile: any = null;


  constructor(

    private keycloakService : KeycloakService,
    private router: Router
  ) {}

  async ngOnInit() {
   /* if (this.keycloakService.isAuthenticated()) {
      console.log('User has an active Keycloak session');
      this.router.navigate(['/espace-user']);
      return;
    } */
  }
  login() {
    this.isLoading = true;
    this.errorMessage = [];
    try {
      this.keycloakService.login();
    } catch (error) {
      console.error("Login failed", error);
      this.isLoading = false;
      this.errorMessage = ['An error occurred during login'];
    }
  }

  logout() {
    try {
      this.keycloakService.logout();
    } catch (error) {
      console.error('Logout failed', error);
    }
  }

  register() {
    this.registerFlag = !this.registerFlag;
  }
}


