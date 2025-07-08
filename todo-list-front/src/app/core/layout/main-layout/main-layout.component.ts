// main-layout.component.ts
import { Component } from '@angular/core';
import { KeycloakService } from '../../../services/keycloak/keycloak.service';

interface User {
  name: string;
  email: string;
  role: string;
  avatar: string;
}

interface MenuItem {
  icon: string;
  text: string;
  route: string;
  category?: string;
  highlight?: boolean;
}

@Component({
  selector: 'app-main-layout',
  templateUrl: './main-layout.component.html',
  styleUrls: ['./main-layout.component.scss']
})
export class MainLayoutComponent {
  isSidenavOpen = true;

  // User data - you can get this from Keycloak or a user service
  user: User = {
    name: 'John Doe',
    email: 'john.doe@company.com',
    role: 'Analyst',
    avatar: 'assets/avatars/default-avatar.png'
  };

  // Menu items for navigation
  menuItems: MenuItem[] = [
    {
      icon: 'dashboard',
      text: 'Dashboard',
      route: '/app/dashboard',
      highlight: true
    },
    {
      icon: 'assignment',
      text: 'New Project',
      route: '/app/dashboard/new-project'
    },
    {
      icon: 'task',
      text: 'Tasks',
      route: '/app/dashboard/tasks'
    },
    {
      icon: 'settings',
      text: 'Settings',
      route: '/app/dashboard/settings'
    },
    {
      icon: 'target',
      text: 'Targets',
      route: '/app/targets'
    }
  ];

  constructor(private keycloakService: KeycloakService) {}

  onToggleSidenav(): void {
    this.isSidenavOpen = !this.isSidenavOpen;
  }

  onLogout(): void {
    this.keycloakService.logout();
  }
}
