// src/app/targets/components/add-target/add-target.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TargetService } from '../../services/target.service';
import { CreateTargetDto } from '../../models/target.model';
import { INDUSTRIES, PRIORITIES, DEAL_STATUSES } from '../../constants/target-form-constants';

@Component({
 selector: 'app-add-target',
 templateUrl: './add-target.component.html',
 styleUrls: ['./add-target.component.scss']
})
export class AddTargetComponent implements OnInit {

 targetForm: FormGroup;
 isLoading = false;
 industries = INDUSTRIES;
 priorities = PRIORITIES;
 dealStatuses = DEAL_STATUSES;


 constructor(
   private fb: FormBuilder,
   private router: Router,
   private snackBar: MatSnackBar,
   private targetService: TargetService
 ) {
   this.targetForm = this.createForm();
 }

 ngOnInit(): void {}

 private createForm(): FormGroup {
   return this.fb.group({
     // Basic Information
     companyName: ['', [Validators.required, Validators.minLength(2)]],
     tickerSymbol: [''],
     website: [''],

     // Financial Information
     marketCap: [''],
     annualRevenue: [''],
     industry: ['', Validators.required],

     // Strategic Information
     priority: ['medium', Validators.required],
     dealStatus: ['research', Validators.required],
     strategicRationale: [''],

     // Additional Details
     headquarters: [''],
     employeeCount: [''],
     foundedYear: ['']
   });
 }

 onSubmit(): void {
   if (this.targetForm.valid) {
     this.isLoading = true;

     const targetDto: CreateTargetDto = this.targetForm.value;
     console.log(" target company created is  --> ",targetDto)
     this.targetService.createTarget(targetDto).subscribe({
       next: (response) => {
        console.log(" target company created is  --> ",targetDto)
         this.snackBar.open('Target company added successfully!', 'Close', {
           duration: 3000,
           panelClass: ['success-snackbar']
         });
         this.router.navigate(['/app/targets', response.id]);
       },
       error: (error) => {
         this.snackBar.open('Error adding target company. Please try again.', 'Close', {
           duration: 5000,
           panelClass: ['error-snackbar']
         });
         this.isLoading = false;
       }
     });
   } else {
     this.markFormGroupTouched();
   }
 }

 onCancel(): void {
   this.router.navigate(['/app/targets']);
 }

 private markFormGroupTouched(): void {
   Object.keys(this.targetForm.controls).forEach(field => {
     const control = this.targetForm.get(field);
     control?.markAsTouched({ onlySelf: true });
   });
 }

 // Helper method for form validation
 getErrorMessage(fieldName: string): string {
   const control = this.targetForm.get(fieldName);
   if (control?.hasError('required')) {
     return `${fieldName} is required`;
   }
   if (control?.hasError('minlength')) {
     return `${fieldName} must be at least ${control.errors?.['minlength'].requiredLength} characters`;
   }
   return '';
 }
}
