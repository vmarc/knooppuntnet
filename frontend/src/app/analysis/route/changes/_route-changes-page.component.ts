import { AsyncPipe } from '@angular/common';
import { NgFor } from '@angular/common';
import { NgIf } from '@angular/common';
import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ChangesComponent } from '@app/analysis/components/changes';
import { ItemComponent } from '@app/components/shared/items';
import { ItemsComponent } from '@app/components/shared/items';
import { LinkLoginComponent } from '@app/components/shared/link';
import { PageComponent } from '@app/components/shared/page';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { selectUserLoggedIn } from '@app/core';
import { Store } from '@ngrx/store';
import { RoutePageHeaderComponent } from '../components/route-page-header.component';
import { actionRouteChangesPageDestroy } from '../store/route.actions';
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
import { RouteChangeComponent } from './route-change.component';
import { RouteChangesSidebarComponent } from './route-changes-sidebar.component';

@Component({
  selector: 'kpn-route-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <ul class="breadcrumb">
        <li><a [routerLink]="'/'" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a [routerLink]="'/analysis'" i18n="@@breadcrumb.analysis"
            >Analysis</a
          >
        </li>
        <li i18n="@@breadcrumb.route-changes">Route changes</li>
      </ul>

      <kpn-route-page-header
        pageName="changes"
        [routeId]="routeId()"
        [routeName]="routeName()"
        [changeCount]="changeCount()"
        [networkType]="networkType()"
      />

      <div *ngIf="apiResponse() as response" class="kpn-spacer-above">
        <div
          *ngIf="!response.result; else routeFound"
          i18n="@@route.route-not-found"
        >
          Route not found
        </div>
        <ng-template #routeFound>
          <div
            *ngIf="loggedIn() === false; else changes"
            i18n="@@route-changes.login-required"
          >
            The details of the route changes history is available to registered
            OpenStreetMap contributors only, after
            <kpn-link-login></kpn-link-login>
            .
          </div>
          <ng-template #changes>
            <div *ngIf="response.result as page">
              <p>
                <kpn-situation-on [timestamp]="response.situationOn" />
              </p>
              <kpn-changes
                [impact]="impact()"
                [pageSize]="pageSize()"
                [pageIndex]="pageIndex()"
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
      <kpn-route-changes-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    AsyncPipe,
    ChangesComponent,
    ItemComponent,
    ItemsComponent,
    LinkLoginComponent,
    NgFor,
    NgIf,
    PageComponent,
    RouteChangeComponent,
    RouteChangesSidebarComponent,
    RoutePageHeaderComponent,
    RouterLink,
    SituationOnComponent,
  ],
})
export class RouteChangesPageComponent implements OnInit, OnDestroy {
  readonly routeId = this.store.selectSignal(selectRouteId);
  readonly routeName = this.store.selectSignal(selectRouteName);
  readonly networkType = this.store.selectSignal(selectRouteNetworkType);
  readonly changeCount = this.store.selectSignal(selectRouteChangeCount);
  readonly impact = this.store.selectSignal(selectRouteChangesPageImpact);
  readonly pageSize = this.store.selectSignal(selectRouteChangesPageSize);
  readonly pageIndex = this.store.selectSignal(selectRouteChangesPageIndex);
  readonly loggedIn = this.store.selectSignal(selectUserLoggedIn);
  readonly apiResponse = this.store.selectSignal(selectRouteChangesPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionRouteChangesPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionRouteChangesPageDestroy());
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
