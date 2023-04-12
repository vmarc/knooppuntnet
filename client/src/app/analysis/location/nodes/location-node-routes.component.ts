import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { LocationNodeInfo } from '@api/common/location';

@Component({
  selector: 'kpn-location-node-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <span
      *ngIf="!hasRouteReferences()"
      class="no-routes"
      i18n="@@location-nodes.no-routes"
    >
      no routes
    </span>

    <div *ngIf="hasRouteReferences()" class="kpn-comma-list route-list">
      <span *ngFor="let ref of node.routeReferences">
        <kpn-link-route [routeId]="ref.id" [title]="ref.name" />
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

  hasRouteReferences(): boolean {
    return this.node.routeReferences && this.node.routeReferences.length > 0;
  }
}
