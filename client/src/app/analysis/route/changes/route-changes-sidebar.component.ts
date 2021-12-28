import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ChangesFilterOption } from '@api/common/changes/filter/changes-filter-option';
import { RouteChangesService } from './route-changes.service';

@Component({
  selector: 'kpn-route-changes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-change-filter [filterOptions]="filterOptions"></kpn-change-filter>
    </kpn-sidebar>
  `,
})
export class RouteChangesSidebarComponent {
  // TODO routeChangesService.filterOptions$ | async
  filterOptions: ChangesFilterOption[] = [];
  constructor(public routeChangesService: RouteChangesService) {}
}
