import { Component, OnInit } from '@angular/core';

interface Task {
  id: number;
  title: string;
  status: string;
}

@Component({
  selector: 'app-dash-board',
  templateUrl: './dash-board.component.html',
  styleUrl: './dash-board.component.scss'
})

export class DashBoardComponent implements OnInit {
  chartData = [
    { name: 'Mon', value: 4 },
    { name: 'Tue', value: 3 },
    { name: 'Wed', value: 7 },
    { name: 'Thu', value: 2 },
    { name: 'Fri', value: 5 },
  ];

  tasks: Task[] = [
    { id: 1, title: 'Complete API documentation', status: 'In Progress' },
    { id: 2, title: 'Review pull requests', status: 'Pending' },
    { id: 3, title: 'Update user authentication', status: 'Completed' },
    { id: 4, title: 'Fix navigation bug', status: 'In Progress' },
  ];
formatYAxisTick: any;
formatValue: any;

  constructor() {}

  ngOnInit(): void {}

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


