import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { actionUserLogoutLinkClicked } from '../../../core/user/user.actions';

@Component({
  selector: 'kpn-link-logout',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <a (click)="logoutClicked()" i18n="@@link.logout">logout</a> `,
})
export class LinkLogoutComponent {
  constructor(private store: Store<AppState>) {}

  logoutClicked(): void {
    this.store.dispatch(actionUserLogoutLinkClicked());
  }
}
