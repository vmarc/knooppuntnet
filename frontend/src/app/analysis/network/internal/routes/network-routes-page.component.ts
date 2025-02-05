import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PageComponent } from '@app/shared/components/page/page.component';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { RouterService } from '../../../../shared/services/router.service';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { NetworkRouteTableComponent } from './components/network-route-table.component';
import { NetworkRoutesPageService } from './network-routes-page.service';

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

      <!-- TODO      <kpn-filter [filterOptions]="service.filterOptions()" /> -->

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (!response.result) {
            <p i18n="@@network-page.network-not-found">Network not found</p>
          } @else {
            @if (response.result; as page) {
              <p>
                <kpn-situation-on [timestamp]="response.situationOn" />
              </p>
              @if (page.routes.length === 0) {
                <p i18n="@@network-routes.no-routes">No network routes in network</p>
              } @else {
                <kpn-network-route-table
                  [timeInfo]="page.timeInfo"
                  [surveyDateInfo]="page.surveyDateInfo"
                  [routeType]="page.routeType"
                  [routes]="service.filteredRoutes()"
                />
              }
            }
          }
        </div>
      }
    </kpn-page>
  `,
  providers: [NetworkRoutesPageService, RouterService],
  imports: [
    NetworkPageHeaderComponent,
    NetworkRouteTableComponent,
    PageComponent,
    SituationOnComponent,
  ],
})
export class NetworkRoutesPageComponent implements OnInit {
  protected readonly service = inject(NetworkRoutesPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
