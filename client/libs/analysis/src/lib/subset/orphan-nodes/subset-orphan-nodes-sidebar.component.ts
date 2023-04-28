import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SubsetOrphanNodesService } from './subset-orphan-nodes.service';

@Component({
  selector: 'kpn-subset-orphan-nodes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-filter [filterOptions]="filterOptions$ | async" />
    </kpn-sidebar>
  `,
})
export class SubsetOrphanNodesSidebarComponent {
  readonly filterOptions$ = this.subsetOrphanNodesService.filterOptions$;

  constructor(private subsetOrphanNodesService: SubsetOrphanNodesService) {}
}
