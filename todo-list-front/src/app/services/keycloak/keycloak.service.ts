import { Injectable } from '@angular/core';
import Keycloak from 'keycloak-js';

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {


  private _keyCloak : Keycloak | undefined;


  get keycloak(){
    if ( !this._keyCloak){
      this._keyCloak = new Keycloak({
        url: 'http://localhost:8180',
        realm: 'todolist-app',
        clientId: 'todolist-app-dev'
      })

    }
    return this._keyCloak;
  }

  constructor() { }

  async init() {
    console.log(" authenticating the user .... ")
    const authenticated = await this._keyCloak?.init({
      onLoad: 'login-required'
    })
    if(authenticated) console.log(" user is authenticated ")
  }

  login() {
    this._keyCloak?.login();
  }

  logout() {
    this._keyCloak?.logout() ;
  }

  getToken(): string | undefined {
    return this._keyCloak?.token;
  }

  isAuthenticated(): boolean {
    return this._keyCloak?.authenticated || false;
  }

  getUserProfile() {
    return this._keyCloak?.loadUserProfile();
  }
}
