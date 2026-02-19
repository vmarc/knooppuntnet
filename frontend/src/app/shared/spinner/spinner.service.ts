import { Injectable } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { finalize } from 'rxjs';
import { of } from 'rxjs';
import { Observable } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { concatMap } from 'rxjs/operators';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class SpinnerService {
  private activeActions: string[] = [];
  private readonly _spinnerState$ = new BehaviorSubject<boolean>(false);
  readonly showSpinner = toSignal(this._spinnerState$.pipe(debounceTime(300)));

  showUntilCompleted<T>(obs$: Observable<T>, action: string): Observable<T> {
    return of(null).pipe(
      tap(() => this.start(action)),
      concatMap(() => obs$),
      finalize(() => this.end(action))
    );
  }

  start(action: string): void {
    this.activeActions.push(action);
    if (this._spinnerState$.value !== true) {
      this._spinnerState$.next(true);
    }
  }

  end(action: string): void {
    this.activeActions = this.activeActions.filter((a) => a !== action);
    if (this.activeActions.length === 0 && this._spinnerState$.value !== false) {
      this._spinnerState$.next(false);
    }
  }
}
