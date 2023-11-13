import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { actionUserLoginLinkClicked } from '@app/core';
import { Store } from '@ngrx/store';

@Component({
  selector: 'kpn-link-login',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a rel="nofollow noreferrer" (click)="login()" i18n="@@login">login</a>
  `,
  standalone: true,
})
export class LinkLoginComponent {
  constructor(private store: Store) {}

  login(): void {
    this.store.dispatch(actionUserLoginLinkClicked());
  }
}
