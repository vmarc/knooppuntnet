import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { actionUserLoginLinkClicked } from '../../../core/user/user.actions';

@Component({
  selector: 'kpn-link-login',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a rel="nofollow noreferrer" (click)="login()" i18n="@@login">login</a>
  `,
})
export class LinkLoginComponent {
  constructor(private store: Store) {}

  login(): void {
    this.store.dispatch(actionUserLoginLinkClicked());
  }
}
