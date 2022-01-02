import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { filter } from 'rxjs/operators';
import { AppState } from '../../../core/core.state';
import { UserService } from '../../../services/user.service';
import { actionRouteChangesPageIndex } from '../store/route.actions';
import { actionRouteChangesPageInit } from '../store/route.actions';
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
    >
    </kpn-route-page-header>

    <div class="kpn-spacer-above">
      <div *ngIf="!isLoggedIn()" i18n="@@route-changes.login-required">
        The details of the route changes history is available to registered
        OpenStreetMap contributors only, after
        <kpn-link-login></kpn-link-login>
        .
      </div>

      <div *ngIf="response$ | async as response">
        <div *ngIf="!response.result" i18n="@@route.route-not-found">
          Route not found
        </div>
        <div *ngIf="response.result as page">
          <p>
            <kpn-situation-on
              [timestamp]="response.situationOn"
            ></kpn-situation-on>
          </p>
          <kpn-changes
            [totalCount]="page.totalCount"
            [changeCount]="page.changeCount"
            [pageIndex]="pageIndex$ | async"
            (pageIndexChange)="pageIndexChanged($event)"
          >
            <kpn-items>
              <kpn-item
                *ngFor="let routeChangeInfo of page.changes"
                [index]="routeChangeInfo.rowIndex"
              >
                <kpn-route-change
                  [routeChangeInfo]="routeChangeInfo"
                ></kpn-route-change>
              </kpn-item>
            </kpn-items>
          </kpn-changes>
        </div>
      </div>
    </div>
  `,
})
export class RouteChangesPageComponent implements OnInit {
  readonly routeId$ = this.store.select(selectRouteId);
  readonly routeName$ = this.store.select(selectRouteName);
  readonly networkType$ = this.store.select(selectRouteNetworkType);
  readonly changeCount$ = this.store.select(selectRouteChangeCount);
  readonly totalCount$ = this.store.select(selectRouteChangeCount);
  readonly pageIndex$ = this.store.select(selectRouteChangesPageIndex);

  readonly response$ = this.store
    .select(selectRouteChangesPage)
    .pipe(filter((x) => x !== null));

  constructor(
    private userService: UserService,
    private store: Store<AppState>
  ) {}

  ngOnInit(): void {
    if (this.isLoggedIn()) {
      this.store.dispatch(actionRouteChangesPageInit());
    }
  }

  isLoggedIn(): boolean {
    return this.userService.isLoggedIn();
  }

  pageIndexChanged(pageIndex: number): void {
    this.store.dispatch(actionRouteChangesPageIndex({ pageIndex }));
  }
}
