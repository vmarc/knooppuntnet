import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { selectDefined } from '@app/core';
import { selectUserLoggedIn } from '@app/core/user';
import { Store } from '@ngrx/store';
import { actionRouteChangesPageSize } from '../store/route.actions';
import { actionRouteChangesPageImpact } from '../store/route.actions';
import { actionRouteChangesPageIndex } from '../store/route.actions';
import { actionRouteChangesPageInit } from '../store/route.actions';
import { selectRouteChangesPageSize } from '../store/route.selectors';
import { selectRouteChangesPageImpact } from '../store/route.selectors';
import { selectRouteChangesPage } from '../store/route.selectors';
import { selectRouteNetworkType } from '../store/route.selectors';
import { selectRouteChangesPageIndex } from '../store/route.selectors';
import { selectRouteChangeCount } from '../store/route.selectors';
import { selectRouteName } from '../store/route.selectors';
import { selectRouteId } from '../store/route.selectors';

@Component({
  selector: 'kpn-route-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a [routerLink]="'/'" i18n="@@breadcrumb.home">Home</a></li>
      <li>
        <a [routerLink]="'/analysis'" i18n="@@breadcrumb.analysis">Analysis</a>
      </li>
      <li i18n="@@breadcrumb.route-changes">Route changes</li>
    </ul>

    <kpn-route-page-header
      pageName="changes"
      [routeId]="routeId$ | async"
      [routeName]="routeName$ | async"
      [changeCount]="changeCount$ | async"
      [networkType]="networkType$ | async"
    />

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div
        *ngIf="!response.result; else routeFound"
        i18n="@@route.route-not-found"
      >
        Route not found
      </div>
      <ng-template #routeFound>
        <div
          *ngIf="(loggedIn$ | async) === false; else loggedIn"
          i18n="@@route-changes.login-required"
        >
          The details of the route changes history is available to registered
          OpenStreetMap contributors only, after
          <kpn-link-login></kpn-link-login>
          .
        </div>
        <ng-template #loggedIn>
          <div *ngIf="response.result as page">
            <p>
              <kpn-situation-on [timestamp]="response.situationOn" />
            </p>
            <kpn-changes
              [impact]="impact$ | async"
              [pageSize]="pageSize$ | async"
              [pageIndex]="pageIndex$ | async"
              (impactChange)="onImpactChange($event)"
              (pageSizeChange)="onPageSizeChange($event)"
              (pageIndexChange)="onPageIndexChange($event)"
              [totalCount]="page.totalCount"
              [changeCount]="page.changeCount"
            >
              <kpn-items>
                <kpn-item
                  *ngFor="let routeChangeInfo of page.changes"
                  [index]="routeChangeInfo.rowIndex"
                >
                  <kpn-route-change [routeChangeInfo]="routeChangeInfo" />
                </kpn-item>
              </kpn-items>
            </kpn-changes>
          </div>
        </ng-template>
      </ng-template>
    </div>
  `,
})
export class RouteChangesPageComponent implements OnInit {
  readonly routeId$ = this.store.select(selectRouteId);
  readonly routeName$ = this.store.select(selectRouteName);
  readonly networkType$ = this.store.select(selectRouteNetworkType);
  readonly changeCount$ = this.store.select(selectRouteChangeCount);
  readonly impact$ = this.store.select(selectRouteChangesPageImpact);
  readonly pageSize$ = this.store.select(selectRouteChangesPageSize);
  readonly pageIndex$ = this.store.select(selectRouteChangesPageIndex);
  readonly loggedIn$ = this.store.select(selectUserLoggedIn);
  readonly response$ = selectDefined(this.store, selectRouteChangesPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionRouteChangesPageInit());
  }

  onImpactChange(impact: boolean): void {
    this.store.dispatch(actionRouteChangesPageImpact({ impact }));
  }

  onPageSizeChange(pageSize: number): void {
    this.store.dispatch(actionRouteChangesPageSize({ pageSize }));
  }

  onPageIndexChange(pageIndex: number): void {
    this.store.dispatch(actionRouteChangesPageIndex({ pageIndex }));
  }
}
