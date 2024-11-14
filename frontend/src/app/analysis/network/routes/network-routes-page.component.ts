import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OldPageComponent } from '@app/components/shared/page';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { RouterService } from '../../../shared/services/router.service';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { NetworkRouteTableComponent } from './components/network-route-table.component';
import { NetworkRoutesSidebarComponent } from './components/network-routes-sidebar.component';
import { NetworkRoutesPageService } from './network-routes-page.service';

@Component({
  selector: 'kpn-network-routes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-old-page>
      <kpn-network-page-header
        pageName="routes"
        pageTitle="Routes"
        i18n-pageTitle="@@network-routes.title"
      />

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
                  [networkType]="page.networkType"
                  [routes]="service.filteredRoutes()"
                />
              }
            }
          }
        </div>
      }
      <kpn-network-routes-sidebar sidebar />
    </kpn-old-page>
  `,
  providers: [NetworkRoutesPageService, RouterService],
  standalone: true,
  imports: [
    NetworkPageHeaderComponent,
    NetworkRouteTableComponent,
    NetworkRoutesSidebarComponent,
    OldPageComponent,
    SituationOnComponent,
  ],
})
export class NetworkRoutesPageComponent implements OnInit {
  protected readonly service = inject(NetworkRoutesPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
