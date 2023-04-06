import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { actionUserAuthenticated } from '@app/core/user/user.actions';

@Component({
  selector: 'kpn-authenticate-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <span i18n="@@authenticate-page.title"> Logging in... </span> `,
})
export class AuthenticatePageComponent implements OnInit {
  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionUserAuthenticated());
  }
}
