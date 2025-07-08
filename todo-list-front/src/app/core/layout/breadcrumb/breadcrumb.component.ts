// src/app/shared/components/breadcrumb/breadcrumb.component.ts
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs';

interface BreadcrumbItem {
  label: string;
  route?: string;
  icon?: string;
}

@Component({
  selector: 'app-breadcrumb',
  templateUrl: './breadcrumb.component.html',
  styleUrls: ['./breadcrumb.component.scss']
})

export class BreadcrumbComponent implements OnInit {
  items: any[] = [];

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit() {
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        this.items = this.getBreadcrumbData(this.activatedRoute);
      });

    // Initial load
    this.items = this.getBreadcrumbData(this.activatedRoute);
  }

  /*private getBreadcrumbData(route: ActivatedRoute): any[] {
    let child = route.firstChild;
    while (child) {
      if (child.snapshot.data['breadcrumb']) {
        return child.snapshot.data['breadcrumb'];
      }
      child = child.firstChild;
    }
    return [];
  }*/
 // src/app/shared/components/breadcrumb/breadcrumb.component.ts
// src/app/shared/components/breadcrumb/breadcrumb.component.ts
private getBreadcrumbData(route: ActivatedRoute): any[] {
  let currentRoute: ActivatedRoute | null = route.root;

  // Find the deepest route with breadcrumb data
  while (currentRoute) {
    if (currentRoute.firstChild && currentRoute.firstChild.snapshot.data['breadcrumb']) {
      return currentRoute.firstChild.snapshot.data['breadcrumb'];
    }
    if (currentRoute.snapshot.data['breadcrumb']) {
      return currentRoute.snapshot.data['breadcrumb'];
    }
    currentRoute = currentRoute.firstChild;
  }

  return [];
}
}

