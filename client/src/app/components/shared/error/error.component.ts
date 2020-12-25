import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from '../../../core/core.state';
import {selectSharedHttpError} from '../../../core/shared/shared.selectors';

@Component({
  selector: 'kpn-error',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="httpError$ | async as httpError" class="kpn-spacer-above http-error">
      <div *ngIf="httpError == 'error-502'">
        <p i18n="error.http.text">Sorry, cannot connect to server at this moment, please try again later.</p>
        <p i18n="error.502.code">(502 - Bad Gateway)</p>
      </div>
      <div *ngIf="httpError == 'error-504'">
        <p i18n="error.504.text">Sorry, cannot connect to server at this moment, please try again later.</p>
        <p i18n="error.504.code">(504 - Gateway timeout)</p>
      </div>
      <div *ngIf="httpError == 'error-0'">
        <p i18n="error.0.text">Sorry, cannot connect to server at this moment, please try again later.</p>
        <p i18n="error.0.code">(0 - No network)</p>
      </div>
      <div *ngIf="httpError == 'error-404'">
        <p i18n="error.404.text">Sorry, cannot get data from server.</p>
        <p i18n="error.404.code">(404 - Not found)</p>
      </div>
      <span *ngIf="httpError != 'error-502' && httpError != 'error-504' && httpError != 'error-0' && httpError != 'error-404'">
        {{httpError}}
      </span>
    </div>
  `,
  styles: [`
    .http-error {
      color: red;
    }
  `]
})
export class ErrorComponent {

  readonly httpError$ = this.store.select(selectSharedHttpError);

  constructor(private store: Store<AppState>) {
  }

}
