import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { actionUserLogoutLinkClicked } from '@app/core/user';
import { Store } from '@ngrx/store';

@Component({
  selector: 'kpn-link-logout',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <a (click)="logoutClicked()" i18n="@@link.logout">logout</a> `,
})
export class LinkLogoutComponent {
  constructor(private store: Store) {}

  logoutClicked(): void {
    this.store.dispatch(actionUserLogoutLinkClicked());
  }
}
