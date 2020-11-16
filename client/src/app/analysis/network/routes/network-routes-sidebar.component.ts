import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {NetworkRoutesService} from './network-routes.service';

@Component({
  selector: 'kpn-network-routes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-filter [filterOptions]="networkRoutesService.filterOptions$ | async"></kpn-filter>
    </kpn-sidebar>
  `
})
export class NetworkRoutesSidebarComponent {
  constructor(public networkRoutesService: NetworkRoutesService) {
  }
}
