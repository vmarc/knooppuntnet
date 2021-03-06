import { HttpErrorResponse } from '@angular/common/http';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { of } from 'rxjs';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { finalize } from 'rxjs/operators';
import { AppState } from '../core/core.state';
import { actionSharedHttpError } from '../core/shared/shared.actions';
import { SpinnerService } from './spinner.service';

@Injectable()
export class SpinnerInterceptor implements HttpInterceptor {
  constructor(
    private spinnerService: SpinnerService,
    private store: Store<AppState>
  ) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const action = 'http-request';
    this.spinnerService.start(action);
    return next.handle(request).pipe(
      catchError((error) => {
        let httpError = 'error';
        if (error instanceof HttpErrorResponse) {
          if (error.error instanceof ErrorEvent) {
            httpError = 'error-event';
          } else {
            httpError = 'error-' + error.status;
          }
        }
        this.store.dispatch(actionSharedHttpError({ httpError }));
        return of(null);
      }),
      finalize(() => this.spinnerService.end(action))
    );
  }
}
