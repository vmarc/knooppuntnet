import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SubsetOrphanRoutesService } from './subset-orphan-routes.service';

@Component({
  selector: 'kpn-subset-orphan-routes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-filter [filterOptions]="filterOptions$ | async"></kpn-filter>
    </kpn-sidebar>
  `,
})
export class SubsetOrphanRoutesSidebarComponent {
  filterOptions$ = this.subsetOrphanRoutesService.filterOptions$;

  constructor(private subsetOrphanRoutesService: SubsetOrphanRoutesService) {}
}
