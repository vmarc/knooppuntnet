import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { actionLocationRoutesPageInit } from '../store/location.actions';
import { selectLocationRoutesPage } from '../store/location.selectors';

@Component({
  selector: 'kpn-location-routes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-location-page-header
      pageName="routes"
      pageTitle="Routes"
      i18n-pageTitle="@@location-routes.title"
    >
    </kpn-location-page-header>

    <kpn-error></kpn-error>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <kpn-location-response [response]="response">
        <kpn-location-routes [page]="response.result"></kpn-location-routes>
      </kpn-location-response>
    </div>
  `,
})
export class LocationRoutesPageComponent implements OnInit {
  readonly response$ = this.store.select(selectLocationRoutesPage);

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionLocationRoutesPageInit());
  }
}
