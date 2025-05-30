import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PageComponent } from '@app/shared/components/page/page.component';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { RouterService } from '@app/shared/services/router.service';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { NetworkRouteTableComponent } from './components/network-route-table.component';
import { NetworkRoutesPageService } from './network-routes-page.service';

@Component({
  selector: 'ui-network-routes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-network-page-header
        pageName="routes"
        pageTitle="Routes"
        i18n-pageTitle="@@network-routes.title"
      />

      <!-- TODO      <ui-filter [filterOptions]="service.filterOptions()" /> -->

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (!response.result) {
            <p i18n="@@network-page.network-not-found">Network not found</p>
          } @else {
            @if (response.result; as page) {
              <p>
                <ui-situation-on [timestamp]="response.situationOn" />
              </p>
              @if (page.routes.length === 0) {
                <p i18n="@@network-routes.no-routes">No network routes in network</p>
              } @else {
                <ui-network-route-table
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
    </ui-page>
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
