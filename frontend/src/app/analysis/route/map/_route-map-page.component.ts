import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PageComponent } from '@app/components/shared/page';
import { AnalysisSidebarComponent } from '@app/components/shared/sidebar';
import { Store } from '@ngrx/store';
import { RoutePageHeaderComponent } from '../components/route-page-header.component';
import { actionRouteMapPageDestroy } from '../store/route.actions';
import { actionRouteMapPageInit } from '../store/route.actions';
import { selectRouteNetworkType } from '../store/route.selectors';
import { selectRouteMapPage } from '../store/route.selectors';
import { selectRouteChangeCount } from '../store/route.selectors';
import { selectRouteName } from '../store/route.selectors';
import { selectRouteId } from '../store/route.selectors';
import { RouteMapComponent } from './route-map.component';

@Component({
  selector: 'kpn-route-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
        </li>
        <li i18n="@@breadcrumb.route-map">Route map</li>
      </ul>

      <kpn-route-page-header
        pageName="map"
        [routeId]="routeId()"
        [routeName]="routeName()"
        [changeCount]="changeCount()"
        [networkType]="networkType()"
      />

      @if (apiResponse(); as response) {
        @if (!response.result) {
          <div class="kpn-spacer-above" i18n="@@route.route-not-found">Route not found</div>
        } @else {
          <kpn-route-map />
        }
      }
      <kpn-analysis-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    AnalysisSidebarComponent,
    AsyncPipe,
    PageComponent,
    RouteMapComponent,
    RoutePageHeaderComponent,
    RouterLink,
  ],
})
export class RouteMapPageComponent implements OnInit, OnDestroy {
  private readonly store = inject(Store);
  protected readonly routeId = this.store.selectSignal(selectRouteId);
  protected readonly routeName = this.store.selectSignal(selectRouteName);
  protected readonly changeCount = this.store.selectSignal(selectRouteChangeCount);
  protected readonly apiResponse = this.store.selectSignal(selectRouteMapPage);
  protected readonly networkType = this.store.selectSignal(selectRouteNetworkType);

  ngOnInit(): void {
    this.store.dispatch(actionRouteMapPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionRouteMapPageDestroy());
  }
}
