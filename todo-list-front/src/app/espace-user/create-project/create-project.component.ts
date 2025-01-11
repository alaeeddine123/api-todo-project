import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { Project, ProjectFormControls} from '../models/project-interface';
import { ProjectPriority } from '../models/project-priority';
import { TeamSize } from '../models/team-size';


@Component({
  selector: 'app-create-project',
  templateUrl: './create-project.component.html',
  styleUrls: ['./create-project.component.scss']
})
export class CreateProjectComponent implements OnInit {

  projectForm: FormGroup<ProjectFormControls>;

  priorities: ProjectPriority[] = [
    { value: 'low', viewValue: 'Low' },
    { value: 'medium', viewValue: 'Medium' },
    { value: 'high', viewValue: 'High' }
  ];

  teamSizes: TeamSize[] = [
    { value: 'small', viewValue: 'Small (1-3)' },
    { value: 'medium', viewValue: 'Medium (4-7)' },
    { value: 'large', viewValue: 'Large (8+)' }
  ];

  constructor(private fb: FormBuilder) {
    this.projectForm = this.fb.group<ProjectFormControls>({
      title: this.fb.control('', { validators: [Validators.required], nonNullable: false }),
      description: this.fb.control('', { validators: [Validators.required], nonNullable: false }),
      dueDate: this.fb.control(null, { validators: [Validators.required], nonNullable: false }),
      priority: this.fb.control('', { validators: [Validators.required], nonNullable: false }),
      teamSize: this.fb.control('', { validators: [Validators.required], nonNullable: false })
    });
  }

  ngOnInit(): void {}

  onSubmit(): void {
    if (this.projectForm.valid) {
      const rawValue = this.projectForm.getRawValue();

      // Create a project object, ensuring all values are non-null
      const projectData = {
        title: rawValue.title || '',
        description: rawValue.description || '',
        dueDate: rawValue.dueDate || new Date(),
        priority: rawValue.priority || '',
        teamSize: rawValue.teamSize || ''
      };

      this.saveProject(projectData);
    } else {
      this.markFormGroupTouched(this.projectForm);
    }
  }

  onCancel(): void {
    this.projectForm.reset();
  }

  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();
      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }

  getErrorMessage(controlName: string): string {
    const control = this.projectForm.get(controlName);
    if (control?.hasError('required')) {
      return `${controlName.charAt(0).toUpperCase() + controlName.slice(1)} is required`;
    }
    return '';
  }

  private saveProject(project: Project): void {
    console.log('Saving project:', project);
    // Add your API call here
  }
}
