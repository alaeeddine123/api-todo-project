import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddTargetComponent } from './components/add-target/add-target.component';
import { TargetDetailComponent } from './components/target-detail/target-detail.component';
import { TargetListComponent } from './components/target-list/target-list.component';

const routes: Routes = [
  { path: '', component: TargetListComponent },
  { path: 'add', component: AddTargetComponent },
  { path: ':id', component: TargetDetailComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})


export class TargetsRoutingModule { }
