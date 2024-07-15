/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PageResponseBookResponseDto } from '../../models/page-response-book-response-dto';

export interface FindBooksByOwner$Params {
  page?: number;
  size?: number;
}

export function findBooksByOwner(http: HttpClient, rootUrl: string, params?: FindBooksByOwner$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseBookResponseDto>> {
  const rb = new RequestBuilder(rootUrl, findBooksByOwner.PATH, 'get');
  if (params) {
    rb.query('page', params.page, {});
    rb.query('size', params.size, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PageResponseBookResponseDto>;
    })
  );
}

findBooksByOwner.PATH = '/books/owner';
