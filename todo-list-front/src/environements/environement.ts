// src/environments/environment.ts
export const environment = {
  production: false,

  // API Configuration
  apiUrl: 'http://localhost:3001',
  apiTimeout: 30000,

  // Feature Flags
  enableAnalytics: false,
  enableDebugMode: true,

  // App Configuration
  appName: 'M&A Analysis Platform',
  version: '1.0.0',

  // Pagination
  defaultPageSize: 10,
  maxPageSize: 100,

  // UI Configuration
  autoSaveInterval: 30000, // 30 seconds
  toastDuration: 3000
};
