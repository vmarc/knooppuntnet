import { inject } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { PageComponent } from '@app/components/shared/page';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { Store } from '@ngrx/store';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { actionNetworkRoutesPageDestroy } from '../store/network.actions';
import { actionNetworkRoutesPageInit } from '../store/network.actions';
import { selectNetworkRoutesPage } from '../store/network.selectors';
import { NetworkRouteTableComponent } from './network-route-table.component';
import { NetworkRoutesSidebarComponent } from './network-routes-sidebar.component';

@Component({
  selector: 'kpn-network-routes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-network-page-header
        pageName="routes"
        pageTitle="Routes"
        i18n-pageTitle="@@network-routes.title"
      />

      @if (apiResponse(); as response) {
        <div class="kpn-spacer-above">
          @if (!response.result) {
            <p i18n="@@network-page.network-not-found">Network not found</p>
          } @else {
            <p>
              <kpn-situation-on [timestamp]="response.situationOn" />
            </p>
            @if (response.result.routes.length === 0) {
              <p i18n="@@network-routes.no-routes">No network routes in network</p>
            } @else {
              <kpn-network-route-table
                [timeInfo]="response.result.timeInfo"
                [surveyDateInfo]="response.result.surveyDateInfo"
                [networkType]="response.result.networkType"
                [routes]="response.result.routes"
              />
            }
          }
        </div>
      }
      <kpn-network-routes-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    NetworkPageHeaderComponent,
    NetworkRouteTableComponent,
    NetworkRoutesSidebarComponent,
    PageComponent,
    SituationOnComponent,
  ],
})
export class NetworkRoutesPageComponent implements OnInit, OnDestroy {
  private readonly store = inject(Store);
  protected readonly apiResponse = this.store.selectSignal(selectNetworkRoutesPage);

  ngOnInit(): void {
    this.store.dispatch(actionNetworkRoutesPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionNetworkRoutesPageDestroy());
  }
}
