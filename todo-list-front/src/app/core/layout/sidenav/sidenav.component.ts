import { Component, Input } from '@angular/core';


interface MenuItem {
  icon: string;
  text: string;
  route: string;
  category?: string;
  highlight?: boolean;
}

@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrl: './sidenav.component.scss'
})
export class SidenavComponent {
  @Input() isOpen = true;
  @Input() menuItems: MenuItem[] = [];
}
