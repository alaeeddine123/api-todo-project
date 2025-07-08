// src/app/targets/targets.module.ts

import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

// Angular Material Modules (grouped for better organization)
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatRadioModule } from '@angular/material/radio';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatAutocompleteModule } from '@angular/material/autocomplete';

// Routing
import { TargetsRoutingModule } from './targets-routing.module';

// Components
import { TargetListComponent } from './components/target-list/target-list.component';
import { AddTargetComponent } from './components/add-target/add-target.component';
import { TargetDetailComponent } from './components/target-detail/target-detail.component';

@NgModule({
  declarations: [
    TargetListComponent,
    AddTargetComponent,
    TargetDetailComponent
  ],
  imports: [
    // Angular Core
    CommonModule,
    FormsModule,           // For ngModel
    ReactiveFormsModule,   // For reactive forms
    HttpClientModule,      // For HTTP calls

    // Angular Material
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatTooltipModule,
    MatRadioModule,
    MatCheckboxModule,
    MatAutocompleteModule,

    // Routing (should be last)
    TargetsRoutingModule
  ],
  providers: [
    // Services are already provided in 'root', so no need to add here
  ]
})
export class TargetsModule { }
