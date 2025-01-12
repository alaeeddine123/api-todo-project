import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { EspaceUserRoutingModule } from './espace-user-routing.module';
import { SharedModule } from '../shared/shared.module';
import { TaskListDisplayComponent } from './task-list-display/task-list-display.component';
import { AppModule } from '../app.module';
import { CreateProjectComponent } from './create-project/create-project.component';
import { DashBoardComponent } from './dash-board/dash-board.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';



@NgModule({
  declarations: [
    HomeComponent,
    TaskListDisplayComponent,
    CreateProjectComponent,
    DashBoardComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    EspaceUserRoutingModule,
    SharedModule,

    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatSidenavModule

  ]
})
export class EspaceUserModule { }
