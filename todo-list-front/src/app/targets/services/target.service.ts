// src/app/targets/services/target.service.ts

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { TargetCompany, CreateTargetDto, UpdateTargetDto } from '@targets/models/target.model';
import { environment } from '../../../environements/environement';

@Injectable({
  providedIn: 'root'
})
export class TargetService {
  private readonly API_URL = environment.apiUrl;
  private targetsSubject = new BehaviorSubject<TargetCompany[]>([]);
  public targets$ = this.targetsSubject.asObservable();

  constructor(private http: HttpClient) {
    this.loadTargets();
  }

  // Get all targets
  getTargets(): Observable<TargetCompany[]> {
    return this.http.get<TargetCompany[]>(`${this.API_URL}/targets`)
      .pipe(
        tap(targets => this.targetsSubject.next(targets))
      );
  }

  // Get target by ID
  getTarget(id: number): Observable<TargetCompany> {
    return this.http.get<TargetCompany>(`${this.API_URL}/targets/${id}`);
  }

  // Create new target
  createTarget(target: CreateTargetDto): Observable<TargetCompany> {
    const newTarget = {
      ...target,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    };


    return this.http.post<TargetCompany>(`${this.API_URL}/targets`, newTarget)
      .pipe(
        tap(() => this.loadTargets()) // Refresh the list
      );
  }

  // Update target
  updateTarget(id: number, target: UpdateTargetDto): Observable<TargetCompany> {
    const updatedTarget = {
      ...target,
      updatedAt: new Date().toISOString()
    };

    return this.http.put<TargetCompany>(`${this.API_URL}/targets/${id}`, updatedTarget)
      .pipe(
        tap(() => this.loadTargets()) // Refresh the list
      );
  }

  // Delete target
  deleteTarget(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/targets/${id}`)
      .pipe(
        tap(() => this.loadTargets()) // Refresh the list
      );
  }

  // Search targets
  searchTargets(query: string): Observable<TargetCompany[]> {
    if (!query.trim()) {
      return this.getTargets();
    }

    return this.http.get<TargetCompany[]>(`${this.API_URL}/targets?q=${query}`);
  }

  // Filter targets by priority
  filterByPriority(priority: string): Observable<TargetCompany[]> {
    if (priority === 'ALL') {
      return this.getTargets();
    }

    return this.http.get<TargetCompany[]>(`${this.API_URL}/targets?priorityLevel=${priority}`);
  }

  // Get current targets count
  getTargetsCount(): number {
    return this.targetsSubject.value.length;
  }

  // Get targets by priority
  getTargetsByPriority(priority: string): TargetCompany[] {
    return this.targetsSubject.value.filter(target => target.priorityLevel === priority);
  }

  // Private method to load targets
  private loadTargets(): void {
    this.http.get<TargetCompany[]>(`${this.API_URL}/targets`).subscribe({
      next: (targets) => this.targetsSubject.next(targets),
      error: (error) => console.error('Error loading targets:', error)
    });
  }
}
