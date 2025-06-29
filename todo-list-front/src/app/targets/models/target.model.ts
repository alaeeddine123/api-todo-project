// src/app/targets/models/target.model.ts

export interface TargetCompany {
  id: number;
  companyName: string;
  ticker?: string;
  website?: string;
  industry: string[];
  acquisitionPurpose: string;
  budgetRange: string;
  timeline: string;
  priorityLevel: 'HIGH' | 'MEDIUM' | 'LOW';
  notes?: string;
  createdAt: string;
  updatedAt?: string;
}

export interface CreateTargetDto {
  companyName: string;
  ticker?: string;
  website?: string;
  industry: string[];
  acquisitionPurpose: string;
  budgetRange: string;
  timeline: string;
  priorityLevel: 'HIGH' | 'MEDIUM' | 'LOW';
  notes?: string;
}

export interface UpdateTargetDto extends Partial<CreateTargetDto> {
  id: number;
}

// Form options for dropdowns
export const BUDGET_RANGES = [
  'Under $1B',
  '$1B - $5B',
  '$5B - $20B',
  '$20B - $50B',
  '$50B - $100B',
  'Over $100B'
];

export const TIMELINES = [
  '3 months',
  '6 months',
  '1 year',
  '2+ years'
];

export const PRIORITY_LEVELS = [
  { value: 'HIGH', label: 'High', color: 'warn' },
  { value: 'MEDIUM', label: 'Medium', color: 'primary' },
  { value: 'LOW', label: 'Low', color: 'accent' }
];

export const COMMON_INDUSTRIES = [
  'Technology',
  'FinTech',
  'InsurTech',
  'Electric Vehicles',
  'Automotive',
  'SaaS',
  'Cloud Computing',
  'Data Analytics',
  'Artificial Intelligence',
  'Payments',
  'Insurance',
  'Healthcare',
  'E-commerce',
  'Cybersecurity',
  'Real Estate',
  'Retail',
  'Manufacturing'
];
