import {OnInit} from '@angular/core';
import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {OnDestroy} from '@angular/core';
import {Store} from '@ngrx/store';
import {PageService} from '../../../components/shared/page.service';
import {AppState} from '../../../core/core.state';
import {actionRouteMapPageInit} from '../store/route.actions';
import {selectRouteMapPage} from '../store/route.selectors';
import {selectRouteChangeCount} from '../store/route.selectors';
import {selectRouteName} from '../store/route.selectors';
import {selectRouteId} from '../store/route.selectors';

@Component({
  selector: 'kpn-route-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a></li>
      <li i18n="@@breadcrumb.route-map">Route map</li>
    </ul>

    <kpn-route-page-header
      pageName="map"
      [routeId]="routeId$ | async"
      [routeName]="routeName$ | async"
      [changeCount]="changeCount$ | async">
    </kpn-route-page-header>

    <div *ngIf="response$ | async as response">
      <div *ngIf="!response.result" class="kpn-spacer-above" i18n="@@route.route-not-found">
        Route not found
      </div>
      <div *ngIf="response.result">
        <kpn-route-map [routeInfo]="response.result.route"></kpn-route-map>
      </div>
    </div>
  `
})
export class RouteMapPageComponent implements OnInit, OnDestroy {

  readonly routeId$ = this.store.select(selectRouteId);
  readonly routeName$ = this.store.select(selectRouteName);
  readonly changeCount$ = this.store.select(selectRouteChangeCount);
  readonly response$ = this.store.select(selectRouteMapPage);

  constructor(private pageService: PageService,
              private store: Store<AppState>) {
    this.pageService.showFooter = false;
  }

  ngOnInit(): void {
    this.store.dispatch(actionRouteMapPageInit());
  }

  ngOnDestroy(): void {
    this.pageService.showFooter = true;
  }
}
