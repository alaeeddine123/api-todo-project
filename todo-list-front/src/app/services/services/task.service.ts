/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { findAllTaksByAssignee } from '../fn/task/find-all-taks-by-assignee';
import { FindAllTaksByAssignee$Params } from '../fn/task/find-all-taks-by-assignee';
import { findAllTasks } from '../fn/task/find-all-tasks';
import { FindAllTasks$Params } from '../fn/task/find-all-tasks';
import { findAllTasksByStatus } from '../fn/task/find-all-tasks-by-status';
import { FindAllTasksByStatus$Params } from '../fn/task/find-all-tasks-by-status';
import { findTaskById } from '../fn/task/find-task-by-id';
import { FindTaskById$Params } from '../fn/task/find-task-by-id';
import { PageResponseTaskResponse } from '../models/page-response-task-response';
import { saveTask } from '../fn/task/save-task';
import { SaveTask$Params } from '../fn/task/save-task';
import { TaskResponse } from '../models/task-response';
import { updateShareableStatus } from '../fn/task/update-shareable-status';
import { UpdateShareableStatus$Params } from '../fn/task/update-shareable-status';

@Injectable({ providedIn: 'root' })
export class TaskService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `findAllTasks()` */
  static readonly FindAllTasksPath = '/tasks';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllTasks()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllTasks$Response(params?: FindAllTasks$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseTaskResponse>> {
    return findAllTasks(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllTasks$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllTasks(params?: FindAllTasks$Params, context?: HttpContext): Observable<PageResponseTaskResponse> {
    return this.findAllTasks$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponseTaskResponse>): PageResponseTaskResponse => r.body)
    );
  }

  /** Path part for operation `saveTask()` */
  static readonly SaveTaskPath = '/tasks';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `saveTask()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  saveTask$Response(params: SaveTask$Params, context?: HttpContext): Observable<StrictHttpResponse<number>> {
    return saveTask(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `saveTask$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  saveTask(params: SaveTask$Params, context?: HttpContext): Observable<number> {
    return this.saveTask$Response(params, context).pipe(
      map((r: StrictHttpResponse<number>): number => r.body)
    );
  }

  /** Path part for operation `updateShareableStatus()` */
  static readonly UpdateShareableStatusPath = '/tasks/shareable/{task-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateShareableStatus()` instead.
   *
   * This method doesn't expect any request body.
   */
  updateShareableStatus$Response(params: UpdateShareableStatus$Params, context?: HttpContext): Observable<StrictHttpResponse<number>> {
    return updateShareableStatus(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateShareableStatus$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  updateShareableStatus(params: UpdateShareableStatus$Params, context?: HttpContext): Observable<number> {
    return this.updateShareableStatus$Response(params, context).pipe(
      map((r: StrictHttpResponse<number>): number => r.body)
    );
  }

  /** Path part for operation `findTaskById()` */
  static readonly FindTaskByIdPath = '/tasks/{task-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findTaskById()` instead.
   *
   * This method doesn't expect any request body.
   */
  findTaskById$Response(params: FindTaskById$Params, context?: HttpContext): Observable<StrictHttpResponse<TaskResponse>> {
    return findTaskById(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findTaskById$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findTaskById(params: FindTaskById$Params, context?: HttpContext): Observable<TaskResponse> {
    return this.findTaskById$Response(params, context).pipe(
      map((r: StrictHttpResponse<TaskResponse>): TaskResponse => r.body)
    );
  }

  /** Path part for operation `findAllTasksByStatus()` */
  static readonly FindAllTasksByStatusPath = '/tasks/status';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllTasksByStatus()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllTasksByStatus$Response(params?: FindAllTasksByStatus$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseTaskResponse>> {
    return findAllTasksByStatus(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllTasksByStatus$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllTasksByStatus(params?: FindAllTasksByStatus$Params, context?: HttpContext): Observable<PageResponseTaskResponse> {
    return this.findAllTasksByStatus$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponseTaskResponse>): PageResponseTaskResponse => r.body)
    );
  }

  /** Path part for operation `findAllTaksByAssignee()` */
  static readonly FindAllTaksByAssigneePath = '/tasks/assignee';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllTaksByAssignee()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllTaksByAssignee$Response(params?: FindAllTaksByAssignee$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseTaskResponse>> {
    return findAllTaksByAssignee(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllTaksByAssignee$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllTaksByAssignee(params?: FindAllTaksByAssignee$Params, context?: HttpContext): Observable<PageResponseTaskResponse> {
    return this.findAllTaksByAssignee$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponseTaskResponse>): PageResponseTaskResponse => r.body)
    );
  }

}
