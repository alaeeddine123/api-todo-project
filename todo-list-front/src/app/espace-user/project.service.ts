import { Project } from './models/project-interface';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, map, of, throwError } from 'rxjs';



const baseUrl = 'http://localhost:8088/api/v1/project';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  constructor( private http: HttpClient) { }


  createProject(project: Project): Observable<any> {
    return this.http.post(`${baseUrl}/create-project`, project, {
      withCredentials: true,
      responseType: 'text'
    }).pipe(catchError(error => {
      console.error('project creation error:',error);
      return throwError(()=> error)
    }))
  }

  getAllProjects(): Observable<Project[]> {
    return this.http.get<Project[]>(`${baseUrl}/get-projects-list`, {
      withCredentials: true,
    }).pipe(
      map((projects) => projects || []), // Ensure an empty array if the response is null or undefined
      catchError((error) => {
        console.error('Error fetching projects:', error);
        return of([]); // Return an empty array in case of an error
      })
    );
  }

}
