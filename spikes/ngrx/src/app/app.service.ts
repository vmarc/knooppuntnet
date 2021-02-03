import {HttpClient} from '@angular/common/http';
import {HttpErrorResponse} from '@angular/common/http';
import {BehaviorSubject} from 'rxjs';
import {Observable} from 'rxjs';
import {of} from 'rxjs';
import {Injectable} from '@angular/core';
import {NodeDetailsPage} from './api/node-details-page';
import {ApiResponse} from './api/api-response';
import {catchError} from 'rxjs/operators';
import {map} from 'rxjs/operators';

@Injectable()
export class AppService {

  readonly httpError$: Observable<string>;
  private myHttpError$ = new BehaviorSubject(null);

  constructor(private http: HttpClient) {
    this.httpError$ = this.myHttpError$.asObservable();
  }

  public nodeDetails(nodeId: string): Observable<ApiResponse<NodeDetailsPage>> {
    const url = `/json-api/node/${nodeId}`;
    return this.httpGet(url);
    //   .pipe(
    //   map(response => ApiResponse.fromJSON(response, NodeDetailsPage.fromJSON))
    // );
  }

  private httpGet(url: string): Observable<ApiResponse<any>> {
    this.myHttpError$.next(null);
    return this.http.get(url).pipe(
      map(r => r as ApiResponse<any>),
      catchError((error) => this.handleError(error))
    );
  }

  private handleError(error: any): Observable<ApiResponse<any>> {
    if (error instanceof HttpErrorResponse) {
      if (error.error instanceof ErrorEvent) {
        this.myHttpError$.next('error-event');
      } else {
        this.myHttpError$.next('error-' + error.status);
      }
    } else {
      this.myHttpError$.next('error');
    }
    return of(null /*new ApiResponse<any>()*/);
  }

}
