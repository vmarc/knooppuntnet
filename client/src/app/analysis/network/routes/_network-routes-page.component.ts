import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { actionNetworkRoutesPageInit } from '../store/network.actions';
import { selectNetworkRoutesPage } from '../store/network.selectors';

@Component({
  selector: 'kpn-network-routes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-network-page-header
      pageName="routes"
      pageTitle="Routes"
      i18n-pageTitle="@@network-routes.title"
    />

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result">
        <p i18n="@@network-page.network-not-found">Network not found</p>
      </div>
      <div *ngIf="response.result">
        <p>
          <kpn-situation-on [timestamp]="response.situationOn" />
        </p>
        <div
          *ngIf="response.result.routes.length === 0"
          i18n="@@network-routes.no-routes"
        >
          No network routes in network
        </div>
        <kpn-network-route-table
          *ngIf="response.result.routes.length > 0"
          [timeInfo]="response.result.timeInfo"
          [surveyDateInfo]="response.result.surveyDateInfo"
          [networkType]="response.result.networkType"
          [routes]="response.result.routes"
        />
      </div>
    </div>
  `,
})
export class NetworkRoutesPageComponent implements OnInit {
  readonly response$ = this.store.select(selectNetworkRoutesPage);

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionNetworkRoutesPageInit());
  }
}
