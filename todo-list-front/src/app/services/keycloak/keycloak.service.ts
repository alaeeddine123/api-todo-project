import { Injectable } from '@angular/core';
import Keycloak from 'keycloak-js';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {
  private _keycloak: Keycloak;
  private _initialized = false;
  private _initPromise: Promise<boolean> | null = null;
  private _authenticationState = new BehaviorSubject<boolean>(false);
  isAuthenticated$: Observable<boolean> = this._authenticationState.asObservable();

  constructor() {
    this._keycloak = new Keycloak({
      url: 'http://localhost:8180',
      realm: 'todolist-app',
      clientId: 'todolist-app-front-dev'
    });
  }

  get instance(): Keycloak {
    return this._keycloak;
  }

  get authServerUrl(): string | undefined {
    return this._keycloak.authServerUrl;
  }

  init(): Promise<boolean> {
    if (this._initPromise) {
      return this._initPromise;
    }

    this._initPromise = new Promise<boolean>((resolve) => {
      const hasAuthParams = window.location.hash.includes('code=') || window.location.hash.includes('state=');

      this._keycloak.init({
        onLoad: 'check-sso' as const,
        silentCheckSsoRedirectUri: window.location.origin + '/assets/silent-check-sso.html',
        checkLoginIframe: false
      })
      .then(authenticated => {
        console.log("Keycloak initialized successfully, authenticated:", authenticated);
        this._initialized = true;
        this._initPromise = null;
        this._authenticationState.next(authenticated);

        if (authenticated) {
          console.log("User has an active Keycloak session");
          this.setupTokenRefresh();
          if (hasAuthParams) {
            window.history.replaceState(null, document.title, window.location.pathname + window.location.search);
            console.log("URL hash cleaned after successful authentication");
          }
        }
        resolve(authenticated);
      })
      .catch(error => {
        console.error("Keycloak initialization failed:", error);
        this._initialized = false;
        this._initPromise = null;
        this._authenticationState.next(false);
        resolve(false);
      });
    });

    return this._initPromise;
  }

  private setupTokenRefresh(): void {
    const minValidity = 70;
    this._keycloak.onTokenExpired = () => {
      console.log("Token expired, refreshing...");
      this._keycloak.updateToken(minValidity).catch(() => {
        console.error("Failed to refresh token, logging out");
        this.logout();
      });
    };
  }

  login(): void {
    this._keycloak.login({
      redirectUri: window.location.origin + '/espace-user/dashboard'
    });
  }

  logout(): void {
    this._keycloak.logout({
      redirectUri: window.location.origin + '/auth/login'
    });
  }

  getToken(): string | undefined {
    return this._keycloak.token;
  }

  isAuthenticated(): boolean {
    return this._initialized && !!this._keycloak.authenticated;
  }

  getUserRoles(): string[] {
    return this._keycloak.realmAccess?.roles || [];
  }

  getUserProfile(): Promise<any> {
    if (!this._initialized) {
      return Promise.reject('Keycloak not initialized');
    }
    if (!this._keycloak.authenticated) {
      return Promise.reject('User not authenticated');
    }
    return this._keycloak.loadUserProfile();
  }
}
