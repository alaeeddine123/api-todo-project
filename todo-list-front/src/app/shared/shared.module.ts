import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

// Component Imports
import { HeaderComponent } from './header/header.component';
import { LayoutComponent } from './layout/layout.component';

// Angular Material Imports
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTooltipModule } from '@angular/material/tooltip';

// Third Party Modules
import { NgxChartsModule } from '@swimlane/ngx-charts';

const materialModules = [
  // Form Controls
  MatFormFieldModule,
  MatInputModule,
  MatSelectModule,
  MatDatepickerModule,
  MatNativeDateModule,

  // Navigation
  MatMenuModule,
  MatSidenavModule,
  MatToolbarModule,

  // Layout
  MatCardModule,
  MatDividerModule,
  MatListModule,

  // Buttons & Indicators
  MatButtonModule,
  MatIconModule,
  MatChipsModule,
  MatTooltipModule,
];

const sharedComponents = [
  HeaderComponent,
  LayoutComponent
];

@NgModule({
  declarations: [
    ...sharedComponents
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NgxChartsModule,
    ...materialModules
  ],
  exports: [
    CommonModule,
    ReactiveFormsModule,
    NgxChartsModule,
    ...materialModules,
    ...sharedComponents
  ]
})
export class SharedModule { }
