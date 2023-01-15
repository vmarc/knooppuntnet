import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { NetworkNodeRow } from '@api/common/network/network-node-row';

@Component({
  selector: 'kpn-network-node-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <span
      *ngIf="node.routeReferences.length === 0"
      class="no-routes"
      i18n="@@network-nodes.no-routes"
    >
      no routes
    </span>

    <div
      *ngIf="node.routeReferences.length > 0"
      class="kpn-comma-list route-list"
    >
      <span *ngFor="let ref of node.routeReferences">
        <kpn-link-route
          [routeId]="ref.id"
          [title]="ref.name"
          [networkType]="ref.networkType"
        />
      </span>
    </div>
  `,
  styles: [
    `
      .no-routes {
        color: red;
      }

      .route-list {
        display: inline-block;
      }
    `,
  ],
})
export class NetworkNodeRoutesComponent {
  @Input() node: NetworkNodeRow;
}
