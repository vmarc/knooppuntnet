import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { actionSharedRegisterLoginCallbackPage } from '../../../core/shared/shared.actions';

@Component({
  selector: 'kpn-link-login',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a (click)="registerLoginCallbackPage()" i18n="@@login">login</a>
  `,
})
export class LinkLoginComponent {
  constructor(private store: Store<AppState>) {}

  registerLoginCallbackPage() {
    this.store.dispatch(actionSharedRegisterLoginCallbackPage());
  }
}
