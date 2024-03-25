import { HttpContextToken } from '@angular/common/http';
import { HttpErrorResponse } from '@angular/common/http';
import { HttpEvent } from '@angular/common/http';
import { HttpHandler } from '@angular/common/http';
import { HttpInterceptor } from '@angular/common/http';
import { HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { throwError } from 'rxjs';
import { of } from 'rxjs';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { SharedStateService } from '../core/shared/shared-state.service';
import { SpinnerService } from './spinner.service';

export const LOCAL_ERROR_HANDLING = new HttpContextToken(() => false);

@Injectable()
export class SpinnerInterceptor implements HttpInterceptor {
  private readonly spinnerService = inject(SpinnerService);
  private readonly sharedStateService = inject(SharedStateService);

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let result$: Observable<HttpEvent<any>>;
    if (request.context.get(LOCAL_ERROR_HANDLING) === true) {
      result$ = next.handle(request);
    } else {
      result$ = next.handle(request).pipe(
        catchError((error) => {
          let httpError = 'error';
          if (error instanceof HttpErrorResponse) {
            if (error.error instanceof ErrorEvent) {
              httpError = 'error-event';
            } else {
              httpError = 'error-' + error.status;
            }
          }
          if (request.url.includes('import?url=https://api.openstreetmap.org/api/0.6')) {
            return throwError(error);
          }
          this.sharedStateService.setHttpError(httpError);
          return of(null);
        })
      );
    }

    return this.spinnerService.showUntilCompleted(result$, 'http-request');
  }
}
