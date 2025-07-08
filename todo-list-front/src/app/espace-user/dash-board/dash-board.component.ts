import { Component, OnInit } from '@angular/core';

interface AnalysisTask {
  id: number;
  companyName: string;
  status: string;
  score?: number;
  recommendation?: string;
}

interface TargetCompany {
  name: string;
  ticker: string;
  currentPrice: string;
  change: number;
  changePercent: number;
  analysisScore?: number;
  status: 'completed' | 'pending' | 'in-progress';
  recommendation?: string;
}

@Component({
  selector: 'app-dash-board',
  templateUrl: './dash-board.component.html',
  styleUrl: './dash-board.component.scss'
})
export class DashBoardComponent implements OnInit {

  // Chart data for weekly analysis overview
  chartData = [
    { name: 'Mon', value: 2 },
    { name: 'Tue', value: 1 },
    { name: 'Wed', value: 4 },
    { name: 'Thu', value: 3 },
    { name: 'Fri', value: 2 },
  ];

  // Recent analysis tasks
  analysisQueue: AnalysisTask[] = [
    { id: 1, companyName: 'Tesla Inc.', status: 'Completed', score: 85, recommendation: 'STRONG BUY' },
    { id: 2, companyName: 'Lemonade Insurance', status: 'In Progress' },
    { id: 3, companyName: 'Stripe Inc.', status: 'Completed', score: 42, recommendation: 'HOLD' },
    { id: 4, companyName: 'Palantir Technologies', status: 'Pending' },
    { id: 5, companyName: 'Snowflake Inc.', status: 'In Progress' },
  ];

  // Target companies with stock prices (mock data)
  targetCompanies: TargetCompany[] = [
    {
      name: 'Tesla Inc.',
      ticker: 'TSLA',
      currentPrice: '$850.32',
      change: 17.82,
      changePercent: 2.1,
      analysisScore: 85,
      status: 'completed',
      recommendation: 'STRONG BUY'
    },
    {
      name: 'Stripe Inc.',
      ticker: 'STRIPE',
      currentPrice: '$95.00B',
      change: -0.76,
      changePercent: -0.8,
      analysisScore: 42,
      status: 'completed',
      recommendation: 'HOLD'
    },
    {
      name: 'Lemonade Inc.',
      ticker: 'LMND',
      currentPrice: '$1.2B',
      change: 0.062,
      changePercent: 5.2,
      status: 'pending'
    },
    {
      name: 'Palantir Technologies',
      ticker: 'PLTR',
      currentPrice: '$45.8B',
      change: 0.687,
      changePercent: 1.5,
      analysisScore: 78,
      status: 'completed',
      recommendation: 'BUY'
    },
    {
      name: 'Snowflake Inc.',
      ticker: 'SNOW',
      currentPrice: '$52.1B',
      change: -1.614,
      changePercent: -3.1,
      status: 'in-progress'
    }
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
      case 'Pending':
        return 'warn';
      default:
        return 'warn';
    }
  }

  getRecommendationColor(recommendation?: string): string {
    if (!recommendation) return 'basic';

    switch (recommendation) {
      case 'STRONG BUY':
        return 'accent';
      case 'BUY':
        return 'primary';
      case 'HOLD':
        return 'warn';
      case 'AVOID':
        return 'warn';
      default:
        return 'basic';
    }
  }

  getChangeColor(change: number): string {
    return change >= 0 ? 'success' : 'danger';
  }

  getChangeIcon(change: number): string {
    return change >= 0 ? 'trending_up' : 'trending_down';
  }

  onViewAnalysis(analysisId: number): void {
    console.log('View analysis details:', analysisId);
    // TODO: Navigate to analysis detail page
  }

  onViewCompany(companyName: string): void {
    console.log('View company details:', companyName);
    // TODO: Navigate to company profile page
  }

  onNewAnalysis(): void {
    console.log('Start new analysis');
    // TODO: Navigate to new analysis page
  }
}
