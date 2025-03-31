import { AuthenticationService } from '../auth.service';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { RegisterRequest } from '../../services/models/register-request';
import { Route, Router } from '@angular/router';



@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'] // Corrected property name
})
export class RegisterComponent {

  requestSent : boolean = false ;

  registrationReq: RegisterRequest = {
    firstname: '',
    lastname: '',
    email: '',
    password: '',
  };


  constructor(
    private http: HttpClient,
    private authenticationService : AuthenticationService,
    private router : Router
    ) {}

  register() {

    console.log("this user value ", this.registrationReq);

    }

    redirectToLogin() {
      this.requestSent = false;
      this.router.navigate(['/login'])
        .then(() => {
          window.location.reload();
        });
    }


}


