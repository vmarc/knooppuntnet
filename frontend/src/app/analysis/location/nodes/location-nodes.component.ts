import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { LocationNodesPage } from '@api/common/location';
import { LocationNodeTableComponent } from './location-node-table.component';

@Component({
  selector: 'kpn-location-nodes',
  changeDetection: ChangeDetectionStrategy.OnPush,

  template: `
    @if (page().nodes.length === 0) {
      <div class="kpn-spacer-above" i18n="@@location-nodes.no-nodes">No nodes</div>
    } @else {
      <kpn-location-node-table
        [timeInfo]="page().timeInfo"
        [nodes]="page().nodes"
        [nodeCount]="page().nodeCount"
      />
    }
  `,
  standalone: true,
  imports: [LocationNodeTableComponent],
})
export class LocationNodesComponent {
  page = input.required<LocationNodesPage>();
}
