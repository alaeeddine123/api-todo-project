import { FormControl } from "@angular/forms";

export interface Project {
  id? : number ;
  title: string;
  description: string;
  dueDate: Date;
  priority: string;
  teamSize: string;
}


export interface ProjectFormControls {
  title: FormControl<string | null>;
  description: FormControl<string | null>;
  dueDate: FormControl<Date | null>;
  priority: FormControl<string | null>;
  teamSize: FormControl<string | null>;
}

