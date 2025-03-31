import { Injectable } from '@angular/core';
import Keycloak from 'keycloak-js';

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {
  private _keycloak: Keycloak;
  private _initialized = false;

  constructor() {
    this._keycloak = new Keycloak({
      url: 'http://localhost:8180',
      realm: 'todolist-app',
      clientId: 'todolist-app-dev'
    });
  }

  init(): Promise<boolean> {
    if (this._initialized) {
      return Promise.resolve(this._keycloak.authenticated || false);
    }

    console.log("Initializing Keycloak...");

    return new Promise<boolean>((resolve) => {
      try {
        // Create the init options with proper type assertions
        const initOptions = {
          onLoad: 'check-sso' as const,
          silentCheckSsoRedirectUri: window.location.origin + '/assets/silent-check-sso.html',
          checkLoginIframe: false, // Disable iframe checks completely for now
          pkceMethod: 'S256' as const
        };

        console.log("Init options:", initOptions);

        this._keycloak.init(initOptions)
          .then(authenticated => {
            console.log("Keycloak initialized successfully, authenticated:", authenticated);

            // Add extra debugging info
            if (this._keycloak.token) {
              const tokenParts = this._keycloak.token.split('.');
              if (tokenParts.length === 3) {
                const payload = JSON.parse(atob(tokenParts[1]));
                console.log("Token expiration:", new Date(payload.exp * 1000));
              }
            }

            this._initialized = true;

            if (authenticated) {
              console.log("User has an active Keycloak session");
              this.setupTokenRefresh();
            } else {
              console.log("No active Keycloak session detected");
            }

            resolve(authenticated);
          })
          .catch(error => {
            console.error("Keycloak initialization failed:", error);
            resolve(false);
          });
      } catch (error) {
        console.error("Exception during Keycloak initialization:", error);
        resolve(false);
      }
    });
  }
  private setupTokenRefresh(): void {
    // Set minimum validity in seconds (refresh when less than 70 seconds remain)
    const minValidity = 70;

    this._keycloak.onTokenExpired = () => {
      console.log("Token expired, refreshing...");
      this._keycloak.updateToken(minValidity)
        .then(refreshed => {
          if (refreshed) {
            console.log("Token was successfully refreshed");
          } else {
            console.log("Token is still valid, not refreshed");
          }
        })
        .catch(error => {
          console.error("Failed to refresh token:", error);
          this.logout();
        });
    };

    // Initial update check
    this._keycloak.updateToken(minValidity).catch(() => {
      console.log("Token update failed on init");
    });
  }

  login(): void {
    if (!this._initialized) {
      console.error("Cannot login: Keycloak not initialized");
      return;
    }

    try {
      const redirectUri = window.location.origin + '/espace-user';
      console.log("Redirecting to:", redirectUri);

      this._keycloak.login({
        redirectUri: redirectUri
      });
    } catch (error) {
      console.error("Error during login:", error);
    }
  }

  logout(): void {
    if (!this._initialized) {
      console.error("Cannot logout: Keycloak not initialized");
      return;
    }

    try {
      const redirectUri = window.location.origin + '/auth/login';
      console.log("Logout redirecting to:", redirectUri);

      this._keycloak.logout({
        redirectUri: redirectUri
      });
    } catch (error) {
      console.error("Error during logout:", error);
    }
  }

  getToken(): string | undefined {
    return this._keycloak.token;
  }

  isAuthenticated(): boolean {
    return this._initialized && !!this._keycloak.authenticated;
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
