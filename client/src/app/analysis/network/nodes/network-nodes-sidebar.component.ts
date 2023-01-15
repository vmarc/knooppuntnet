import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NetworkNodesService } from './network-nodes.service';

@Component({
  selector: 'kpn-network-nodes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-filter [filterOptions]="networkNodesService.filterOptions$ | async" />
    </kpn-sidebar>
  `,
})
export class NetworkNodesSidebarComponent {
  constructor(public networkNodesService: NetworkNodesService) {}
}
