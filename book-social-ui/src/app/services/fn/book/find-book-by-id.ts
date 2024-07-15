/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { BookResponseDto } from '../../models/book-response-dto';

export interface FindBookById$Params {
  'book-id': number;
}

export function findBookById(http: HttpClient, rootUrl: string, params: FindBookById$Params, context?: HttpContext): Observable<StrictHttpResponse<BookResponseDto>> {
  const rb = new RequestBuilder(rootUrl, findBookById.PATH, 'get');
  if (params) {
    rb.path('book-id', params['book-id'], {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<BookResponseDto>;
    })
  );
}

findBookById.PATH = '/books/{book-id}';
