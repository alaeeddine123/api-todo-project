// src/app/core/layout/header/header.component.ts

import { Component, Input, Output, EventEmitter } from '@angular/core';

interface User {
  name: string;
  email: string;
  role: string;
  avatar: string;
}

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  @Input() user!: User;
  @Input() isSidenavOpen = true;

  @Output() toggleSidenav = new EventEmitter<void>();
  @Output() logout = new EventEmitter<void>();

  onToggleSidenav(): void {
    this.toggleSidenav.emit();
  }

  onLogout(): void {
    this.logout.emit();
  }
}
