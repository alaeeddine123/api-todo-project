import { catchError } from 'rxjs';
import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../../auth/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit{

  user = {
    name: 'Alaeeddine Charrik',
    email: 'alaeeddine.charrik1@gmail.com',
    role: 'Administrator',
    avatar: 'assets/avatar.png'
  };

  isSidenavOpen = true;

  menuItems = [
    { icon: 'add_circle', text: 'Create New Project', route: '/new-project' },
    { icon: 'task', text: 'My Tasks', route: '/tasks' },
    { icon: 'folder', text: 'My Projects', route: '/projects' },
    { icon: 'settings', text: 'Settings', route: '/settings' }
  ];
  authService: any;
  router: any;

  constructor(private authenticationService : AuthenticationService) {}

  ngOnInit(): void {}

  onLogout(): void {
    console.log('Logging out...');
    // Implement logout logic
    this.authenticationService.logout().subscribe(); }



  getStatusColor(status: string): string {
    switch (status) {
      case 'Completed':
        return 'accent';
      case 'In Progress':
        return 'primary';
      default:
        return 'warn';
    }
  }

}
