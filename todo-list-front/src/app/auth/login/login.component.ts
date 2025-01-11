import { TokenService } from './../../services/token/token.service';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationRequest } from '../../services/models';
import { finalize } from 'rxjs';
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

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authenticationService : AuthenticationService,
    private tokenService: TokenService
  ) {}

  ngOnInit() {
    this.initializeForm();
  }

  private initializeForm(): void {
    this.registerFlag = false;
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  get email() { return this.loginForm.get('email'); }
  get password() { return this.loginForm.get('password'); }

  login() {
    if (this.loginForm.invalid) {
      this.markFormGroupTouched(this.loginForm);
      console.log(" loginf form",this.loginForm)
      return;
    }

    this.errorMessage = [];
    this.isLoading = true;
    console.log(" loginf form",this.loginForm.value.email , this.loginForm.value.password)
    const authRequest: AuthenticationRequest = {
      email: this.loginForm.value.email,
      password: this.loginForm.value.password
    };


    this.authenticationService.authenticate(authRequest)
    .subscribe({
      next: (response) => {
        console.log("Login successful", response);
          // Navigation is handled in the service after token storage
      },
      error: (error) => {
        console.error("Login failed", error);
        this.isLoading = false;
        if (error.error?.errorMsg) {
          this.errorMessage = [error.error.errorMsg];
        } else {
          this.errorMessage = ['An error occurred during login'];
        }
      },
      complete: () => {
        this.isLoading = false;
      }
    });
}

  private markFormGroupTouched(formGroup: FormGroup) {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();
      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }

  register() {
    this.registerFlag = !this.registerFlag;
  }
}


function register() {
  throw new Error('Function not implemented.');
}

