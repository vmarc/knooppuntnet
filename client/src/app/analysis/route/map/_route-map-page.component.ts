import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { selectDefined } from '@app/core/core.state';
import { actionRouteMapPageInit } from '../store/route.actions';
import { selectRouteNetworkType } from '../store/route.selectors';
import { selectRouteMapPage } from '../store/route.selectors';
import { selectRouteChangeCount } from '../store/route.selectors';
import { selectRouteName } from '../store/route.selectors';
import { selectRouteId } from '../store/route.selectors';

@Component({
  selector: 'kpn-route-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li>
        <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
      </li>
      <li i18n="@@breadcrumb.route-map">Route map</li>
    </ul>

    <kpn-route-page-header
      pageName="map"
      [routeId]="routeId$ | async"
      [routeName]="routeName$ | async"
      [changeCount]="changeCount$ | async"
      [networkType]="networkType$ | async"
    />

    <div *ngIf="response$ | async as response">
      <div
        *ngIf="!response.result"
        class="kpn-spacer-above"
        i18n="@@route.route-not-found"
      >
        Route not found
      </div>
      <div *ngIf="response.result">
        <kpn-route-map />
      </div>
    </div>
  `,
})
export class RouteMapPageComponent implements OnInit {
  readonly routeId$ = this.store.select(selectRouteId);
  readonly routeName$ = this.store.select(selectRouteName);
  readonly changeCount$ = this.store.select(selectRouteChangeCount);
  readonly response$ = selectDefined(this.store, selectRouteMapPage);
  readonly networkType$ = this.store.select(selectRouteNetworkType);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionRouteMapPageInit());
  }
}
