import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { actionUserAuthenticated } from '../../../core/user/user.actions';

@Component({
  selector: 'kpn-authenticate-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <span i18n="@@authenticate-page.title"> Logging in... </span> `,
})
export class AuthenticatePageComponent implements OnInit {
  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionUserAuthenticated());
  }
}
