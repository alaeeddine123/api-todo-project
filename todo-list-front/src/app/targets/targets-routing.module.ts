import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddTargetComponent } from './components/add-target/add-target.component';
import { TargetDetailComponent } from './components/target-detail/target-detail.component';
import { TargetListComponent } from './components/target-list/target-list.component';


const routes: Routes = [
  {
    path: '',
    component: TargetListComponent
    // Uses parent breadcrumb from app routing
  },
  {
    path: 'add',
    component: AddTargetComponent,
    data: {
      breadcrumb: [
        { label: 'Add New Target' }
      ]
    }
  },
  {
    path: ':id',
    component: TargetDetailComponent,
    data: {
      breadcrumb: [
        { label: 'Target Details' }
      ]
    }
  }
 ];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})


export class TargetsRoutingModule { }
