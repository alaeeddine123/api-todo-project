import { Component, OnInit } from '@angular/core';
import { KeycloakService } from '../../services/keycloak/keycloak.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit{

  user = {
    name: 'Alaeeddine Charrik',
    email: 'alaeeddine.charrik1@gmail.com',
    role: 'M&A Analyst', // Updated role
    avatar: 'assets/avatar.png'
  };

  isSidenavOpen = true;

  // Updated menu items for M&A Analysis Platform
  menuItems = [
    {
      icon: 'dashboard',
      text: '📊 Dashboard',
      route: '/dashboard',
      category: 'main'
    },
    {
      icon: 'add_business',
      text: '+ New Analysis',
      route: '/analysis/new',
      category: 'main',
      highlight: true // Special styling for primary action
    },
    {
      icon: 'analytics',
      text: '📈 My Analyses',
      route: '/analyses',
      category: 'main'
    },
    {
      icon: 'business_center',
      text: '🎯 Targets',
      route: '/targets',
      category: 'main'
    },
    {
      icon: 'assessment',
      text: '📋 Reports',
      route: '/reports',
      category: 'main'
    },
    {
      icon: 'trending_up',
      text: '📊 Portfolio View',
      route: '/portfolio',
      category: 'main'
    },
    {
      icon: 'settings',
      text: '⚙️ Settings',
      route: '/settings',
      category: 'main'
    }
  ];

  // Legacy menu items (for transition period - can be removed later)
  legacyMenuItems = [
    {
      icon: 'add_circle',
      text: 'Create New Project',
      route: '/new-project',
      category: 'legacy'
    },
    {
      icon: 'task',
      text: 'My Tasks',
      route: '/tasks',
      category: 'legacy'
    },
    {
      icon: 'folder',
      text: 'My Projects',
      route: '/projects',
      category: 'legacy'
    },
    {
      icon: 'settings',
      text: 'Settings',
      route: '/settings',
      category: 'legacy'
    }
  ];

  authService: any;
  router: any;

  constructor(private keycloakService : KeycloakService) {}

  ngOnInit(): void {}

  onLogout(): void {
    console.log('Logging out...');
    this.keycloakService.logout();
  }

  getStatusColor(status: string): string {
    switch (status) {
      case 'Completed':
      case 'STRONG BUY':
        return 'accent';
      case 'In Progress':
      case 'BUY':
        return 'primary';
      case 'HOLD':
        return 'warn';
      case 'AVOID':
        return 'warn';
      default:
        return 'warn';
    }
  }

  // Helper method to get menu items (can switch between new and legacy)
  getActiveMenuItems() {
    return this.menuItems; // Use new M&A menu
    // return this.legacyMenuItems; // Switch to this if you want legacy menu
  }
}
