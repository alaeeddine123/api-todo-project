// src/app/targets/components/target-list/target-list.component.ts

import { Component, OnInit, OnDestroy } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatIcon } from '@angular/material/icon';
import { Observable, Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { TargetCompany } from '@targets/models/target.model';
import { TargetService } from '../../services/target.service';
import { AddTargetComponent } from '../add-target/add-target.component';

@Component({
  selector: 'app-target-list',
  templateUrl: './target-list.component.html',
  styleUrls: ['./target-list.component.scss']
})
export class TargetListComponent implements OnInit, OnDestroy {
  targets: TargetCompany[] = [];
  filteredTargets: TargetCompany[] = [];
  searchQuery = '';
  selectedPriority = 'ALL';
  isLoading = false;

  priorityOptions = [
    { value: 'ALL', label: 'All Priorities' },
    { value: 'HIGH', label: 'High Priority' },
    { value: 'MEDIUM', label: 'Medium Priority' },
    { value: 'LOW', label: 'Low Priority' }
  ];

  private destroy$ = new Subject<void>();

  constructor(
    private targetService: TargetService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadTargets();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadTargets(): void {
    this.isLoading = true;
    this.targetService.getTargets()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (targets) => {
          this.targets = targets;
          this.applyFilters();
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error loading targets:', error);
          this.showSnackBar('Error loading targets. Please try again.');
          this.isLoading = false;
        }
      });
  }

  onAddTarget(): void {
    const dialogRef = this.dialog.open(AddTargetComponent, {
      width: '600px',
      disableClose: true
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadTargets(); // Refresh the list
        this.showSnackBar('Target added successfully!');
      }
    });
  }

  onEditTarget(target: TargetCompany): void {
    const dialogRef = this.dialog.open(AddTargetComponent, {
      width: '600px',
      disableClose: true,
      data: { target, isEdit: true }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadTargets(); // Refresh the list
        this.showSnackBar('Target updated successfully!');
      }
    });
  }

  onDeleteTarget(target: TargetCompany): void {
    if (confirm(`Are you sure you want to delete "${target.companyName}"?`)) {
      this.targetService.deleteTarget(target.id)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: () => {
            this.loadTargets(); // Refresh the list
            this.showSnackBar('Target deleted successfully!');
          },
          error: (error) => {
            console.error('Error deleting target:', error);
            this.showSnackBar('Error deleting target. Please try again.');
          }
        });
    }
  }

  onStartAnalysis(target: TargetCompany): void {
    // TODO: Navigate to analysis page with target data
    console.log('Starting analysis for:', target.companyName);
    this.showSnackBar(`Analysis for ${target.companyName} will be implemented soon!`);
  }

  onSearch(): void {
    this.applyFilters();
  }

  onFilterByPriority(): void {
    this.applyFilters();
  }

  private applyFilters(): void {
    let filtered = [...this.targets];

    // Apply search filter
    if (this.searchQuery.trim()) {
      const query = this.searchQuery.toLowerCase();
      filtered = filtered.filter(target =>
        target.companyName.toLowerCase().includes(query) ||
        target.ticker?.toLowerCase().includes(query) ||
        target.industry.some(ind => ind.toLowerCase().includes(query)) ||
        target.acquisitionPurpose.toLowerCase().includes(query)
      );
    }

    // Apply priority filter
    if (this.selectedPriority !== 'ALL') {
      filtered = filtered.filter(target => target.priorityLevel === this.selectedPriority);
    }

    this.filteredTargets = filtered;
  }

  getPriorityColor(priority: string): string {
    switch (priority) {
      case 'HIGH': return 'warn';
      case 'MEDIUM': return 'primary';
      case 'LOW': return 'accent';
      default: return 'basic';
    }
  }

  getPriorityIcon(priority: string): string {
    switch (priority) {
      case 'HIGH': return 'priority_high';
      case 'MEDIUM': return 'remove';
      case 'LOW': return 'keyboard_arrow_down';
      default: return 'help';
    }
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    const now = new Date();
    const diffTime = Math.abs(now.getTime() - date.getTime());
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

    if (diffDays === 1) return 'Yesterday';
    if (diffDays < 7) return `${diffDays} days ago`;
    if (diffDays < 30) return `${Math.ceil(diffDays / 7)} weeks ago`;

    return date.toLocaleDateString();
  }

  private showSnackBar(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      horizontalPosition: 'end',
      verticalPosition: 'top'
    });
  }
}
