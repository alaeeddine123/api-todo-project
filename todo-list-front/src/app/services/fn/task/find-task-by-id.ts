/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { TaskResponse } from '../../models/task-response';

export interface FindTaskById$Params {
  'task-id': number;
}

export function findTaskById(http: HttpClient, rootUrl: string, params: FindTaskById$Params, context?: HttpContext): Observable<StrictHttpResponse<TaskResponse>> {
  const rb = new RequestBuilder(rootUrl, findTaskById.PATH, 'get');
  if (params) {
    rb.path('task-id', params['task-id'], {});
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<TaskResponse>;
    })
  );
}

findTaskById.PATH = '/tasks/{task-id}';
