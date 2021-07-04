import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { LocationNodeInfo } from '@api/common/location/location-node-info';

@Component({
  selector: 'kpn-location-node-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <span
      *ngIf="node.routeReferences.isEmpty()"
      class="no-routes"
      i18n="@@location-nodes.no-routes"
    >
      no routes
    </span>

    <div
      *ngIf="node.routeReferences.size > 0"
      class="kpn-comma-list route-list"
    >
      <span *ngFor="let ref of node.routeReferences">
        <kpn-link-route [routeId]="ref.id" [title]="ref.name"></kpn-link-route>
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
export class LocationNodeRoutesComponent {
  @Input() node: LocationNodeInfo;
}
