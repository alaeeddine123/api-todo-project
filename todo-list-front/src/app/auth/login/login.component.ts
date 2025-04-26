import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { KeycloakService } from './../../services/keycloak/keycloak.service';
import { AuthenticationService } from '../auth.service';

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
    //private formBuilder: FormBuilder,
    //private authenticationService : AuthenticationService,
    private keycloakService : KeycloakService,
    private router: Router
  ) {}

  async ngOnInit() {
   // this.initializeForm();
    if (this.keycloakService.isAuthenticated()) {
      console.log('User has an active Keycloak session');
      this.router.navigate(['/espace-user']);
      return;
    }

    // Check local tokens
    /*const token = localStorage.getItem('access_token');
    if (token) {
      console.log('User has local tokens, checking validity');
      // Use your existing auth service to verify token
    }*/
  }

  /*private initializeForm(): void {
    this.registerFlag = false;
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  } */

  get email() { return this.loginForm.get('email'); }
  get password() { return this.loginForm.get('password'); }

  /*async login() {
    if (this.loginForm.invalid) {
      this.markFormGroupTouched(this.loginForm);
      return;
    }

    this.errorMessage = [];
    this.isLoading = true;

    try {
      const request = {
        email: this.loginForm.get('email')?.value,
        password: this.loginForm.get('password')?.value
      };

      // Call your authentication service
      console.log(" request credentials is ",request)
      this.authenticationService.authenticate(request).subscribe({
        next: (response) => {
          // Success is handled in the service (setting tokens and redirecting)
          this.isLoading = false;
        },
        error: (error) => {
          console.error("Login failed", error);
          this.isLoading = false;
          this.errorMessage = ['Invalid credentials'];
        }
      });
    } catch (error) {
      console.error("Login failed", error);
      this.isLoading = false;
      this.errorMessage = ['An error occurred during login'];
    }
  }*/
  /*async login() {
    if (this.loginForm.invalid) {
      this.markFormGroupTouched(this.loginForm);
      return;
    }

    this.errorMessage = [];
    this.isLoading = true;

    try {
      const request = {
        email: this.loginForm.get('email')?.value,
        password: this.loginForm.get('password')?.value
      };

      this.authenticationService.authenticate(request).subscribe({
        next: () => {
          this.isLoading = false;
          // No need to handle redirection here as it's done in the service
        },
        error: (error) => {
          console.error("Login failed", error);
          this.isLoading = false;
          this.errorMessage = ['Invalid credentials'];
        }
      });
    } catch (error) {
      console.error("Login failed", error);
      this.isLoading = false;
      this.errorMessage = ['An error occurred during login'];
    }
  }*/
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

  /*async logout() {
    try {
      console.log('Logging out...');
      await this.authenticationService.logout();
      // If we get here, something went wrong as logout should redirect
    } catch (error) {
      console.error('Logout failed', error);
      this.errorMessage = ['An error occurred during logout'];
    }
  }  */

  /*private markFormGroupTouched(formGroup: FormGroup) {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();
      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  } */

  register() {
    this.registerFlag = !this.registerFlag;
  }
}


