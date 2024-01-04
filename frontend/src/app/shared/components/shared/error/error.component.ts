import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { selectSharedHttpError } from '@app/core';
import { Store } from '@ngrx/store';

@Component({
  selector: 'kpn-error',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (sharedHttpError(); as httpError) {
      <div class="kpn-spacer-above http-error">
        @if (httpError === 'error-500') {
          <p i18n="@@error.500.code">500 - Internal server error</p>
        }
        @if (isRecoverableServerError(httpError)) {
          <p i18n="@@error.http.text">
            Sorry, cannot connect to server at this moment, please try again later.
          </p>
        }
        @if (httpError === 'error-502') {
          <p i18n="@@error.502.code">(502 - Bad Gateway)</p>
        }
        @if (httpError === 'error-504') {
          <p i18n="@@error.504.code">(504 - Gateway timeout)</p>
        }
        @if (httpError === 'error-0') {
          <p i18n="@@error.0.code">(0 - No network)</p>
        }
        @if (httpError === 'error-404') {
          <div>
            <p i18n="@@error.404.text">Sorry, cannot get data from server.</p>
            <p i18n="@@error.404.code">(404 - Not found)</p>
          </div>
        }
        @if (!isKnownError(httpError)) {
          <span>
            {{ httpError }}
          </span>
        }
      </div>
    }
  `,
  styles: `
    .http-error {
      color: red;
    }
  `,
  standalone: true,
  imports: [AsyncPipe],
})
export class ErrorComponent {
  readonly sharedHttpError = this.store.selectSignal(selectSharedHttpError);

  constructor(private store: Store) {}

  isRecoverableServerError(httpError: string): boolean {
    return httpError === 'error-502' || httpError === 'error-504' || httpError === 'error-0';
  }

  isKnownError(httpError: string): boolean {
    return (
      this.isRecoverableServerError(httpError) ||
      httpError === 'error-500' ||
      httpError === 'error-404'
    );
  }
}
