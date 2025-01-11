/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PageResponseTaskResponse } from '../../models/page-response-task-response';

export interface FindAllTasksByStatus$Params {
  page?: number;
  size?: number;
}

export function findAllTasksByStatus(http: HttpClient, rootUrl: string, params?: FindAllTasksByStatus$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseTaskResponse>> {
  const rb = new RequestBuilder(rootUrl, findAllTasksByStatus.PATH, 'get');
  if (params) {
    rb.query('page', params.page, {});
    rb.query('size', params.size, {});
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PageResponseTaskResponse>;
    })
  );
}

findAllTasksByStatus.PATH = '/tasks/status';
