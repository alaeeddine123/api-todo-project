import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

// Material Imports
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatListModule } from '@angular/material/list';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatChipsModule } from '@angular/material/chips';

// App Imports
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthModule } from './auth/auth.module';
import { EspaceUserModule } from './espace-user/espace-user.module';
import { SharedModule } from "./shared/shared.module";
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { AuthInterceptor } from './auth/auth.interceptor';
import { KeycloakService } from './services/keycloak/keycloak.service';
import { Router } from '@angular/router';


function initializeKeycloak(keycloak: KeycloakService, router: Router) {
  return () => {
    console.log("Initializing Keycloak from APP_INITIALIZER");
    return keycloak.init().then(authenticated => {
      console.log("Keycloak initialization complete, authenticated:", authenticated);
      if (authenticated) {
        const hasAuthParams = window.location.hash.includes('code=') || window.location.hash.includes('state=');
        if (hasAuthParams) {
          window.history.replaceState(null, document.title, window.location.pathname + window.location.search);
          console.log("URL hash cleaned in APP_INITIALIZER");
        }
      }
      return authenticated;
    });
  };
}


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    // Material Modules
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatCardModule,
    MatToolbarModule,
    MatIconModule,
    MatDividerModule,
    MatListModule,
    MatChipsModule,
    MatTooltipModule,
    // App Modules
    EspaceUserModule,
    SharedModule,
    AppRoutingModule
  ],
  providers: [
    provideAnimationsAsync(),
    provideHttpClient(withInterceptorsFromDi()),
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService,Router]
    },
    { provide: HTTP_INTERCEPTORS,  useClass: AuthInterceptor,multi: true },

  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
