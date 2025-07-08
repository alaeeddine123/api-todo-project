// src/app/targets/constants/target-form.constants.ts
export const INDUSTRIES = [
  { value: 'technology', label: 'Technology' },
  { value: 'healthcare', label: 'Healthcare' },
  { value: 'finance', label: 'Financial Services' },
  { value: 'manufacturing', label: 'Manufacturing' },
  { value: 'retail', label: 'Retail' },
  { value: 'energy', label: 'Energy' }
] as const;

export const PRIORITIES = [
  { value: 'high', label: '🔴 High Priority', color: 'warn' },
  { value: 'medium', label: '🟡 Medium Priority', color: 'accent' },
  { value: 'low', label: '🟢 Low Priority', color: 'primary' }
] as const;

export const DEAL_STATUSES = [
  { value: 'research', label: '📊 Research Phase' },
  { value: 'contact', label: '📞 Initial Contact' },
  { value: 'negotiation', label: '🤝 Negotiation' },
  { value: 'due_diligence', label: '🔍 Due Diligence' }
] as const;


export const PRIORITY_FILTER_OPTIONS = [
  { value: '', label: 'All Priorities' },
  ...PRIORITIES
] as const;
