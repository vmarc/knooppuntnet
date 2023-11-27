import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FilterComponent } from '@app/analysis/components/filter';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { NetworkNodesService } from './network-nodes.service';

@Component({
  selector: 'kpn-network-nodes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-filter [filterOptions]="networkNodesService.filterOptions$ | async" />
    </kpn-sidebar>
  `,
  standalone: true,
  imports: [SidebarComponent, FilterComponent, AsyncPipe],
})
export class NetworkNodesSidebarComponent {
  constructor(public networkNodesService: NetworkNodesService) {}
}
