import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { actionUserAuthenticated } from '@app/core';
import { Store } from '@ngrx/store';

@Component({
  selector: 'kpn-authenticate-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <span i18n="@@authenticate-page.title"> Logging in... </span> `,
  standalone: true,
})
export class AuthenticatePageComponent implements OnInit {
  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionUserAuthenticated());
  }
}
