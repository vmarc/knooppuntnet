import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { NetworkRouteListComponent } from './components/network-route-list.component';
import { NetworkRoutesPageService } from './network-routes-page.service';

@Component({
  selector: 'ui-network-routes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.response(); as response) {
      @if (response?.result; as page) {
        <p class="kpn-spacer-above">
          <ui-situation-on [timestamp]="response.situationOn" />
        </p>
        @if (page.routes.length === 0) {
          <p i18n="@@network-routes.no-routes">No network routes in network</p>
        } @else {
          <ui-network-route-list
            [timeInfo]="page.timeInfo"
            [surveyDateInfo]="page.surveyDateInfo"
            [routeType]="page.routeType"
            [routes]="service.filteredRoutes()"
          />
        }
      }
    }
  `,
  providers: [NetworkRoutesPageService],
  imports: [NetworkRouteListComponent, SituationOnComponent],
})
export class NetworkRoutesPageComponent implements OnInit {
  readonly service = inject(NetworkRoutesPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
