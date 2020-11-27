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

  private _httpError$ = new BehaviorSubject(null);
  readonly httpError$: Observable<string> = this._httpError$.asObservable();

  constructor(private http: HttpClient) {
  }

  public nodeDetails(nodeId: string): Observable<ApiResponse<NodeDetailsPage>> {
    const url = `/json-api/node/${nodeId}`;
    return this.httpGet(url);
    //   .pipe(
    //   map(response => ApiResponse.fromJSON(response, NodeDetailsPage.fromJSON))
    // );
  }

  private httpGet(url: string): Observable<ApiResponse<any>> {
    this._httpError$.next(null);
    return this.http.get(url).pipe(
      map(r => r as ApiResponse<any>),
      catchError((error) => this.handleError(error))
    );
  }

  private handleError(error: any): Observable<ApiResponse<any>> {
    if (error instanceof HttpErrorResponse) {
      if (error.error instanceof ErrorEvent) {
        this._httpError$.next('error-event');
      } else {
        this._httpError$.next('error-' + error.status);
      }
    } else {
      this._httpError$.next('error');
    }
    return of(null /*new ApiResponse<any>()*/);
  }

}
