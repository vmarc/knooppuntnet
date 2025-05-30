import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { LocationNodesPage } from '@api/common/location/location-nodes-page';
import { LocationNodeListComponent } from './location-node-list.component';

@Component({
  selector: 'ui-location-nodes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (page().nodes.length === 0) {
      <div class="kpn-spacer-above" i18n="@@location-nodes.no-nodes">No nodes</div>
    } @else {
      <ui-location-node-list
        [timeInfo]="page().timeInfo"
        [nodes]="page().nodes"
        [nodeCount]="page().nodeCount"
      />
    }
  `,
  imports: [LocationNodeListComponent],
})
export class LocationNodesComponent {
  page = input.required<LocationNodesPage>();
}
