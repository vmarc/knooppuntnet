import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { LocationNodeInfo } from '@api/common/location/location-node-info';
import { LinkRouteComponent } from '@app/shared/components/link/link-route.component';

@Component({
  selector: 'ui-location-node-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (!hasRouteReferences()) {
      <span class="color-error" i18n="@@location-nodes.no-routes"> no routes </span>
    } @else {
      <span class="kpn-comma-list">
        @for (ref of node().routeReferences; track ref) {
          <span>
            <ui-link-route [routeId]="ref.id" [routeName]="ref.name" />
          </span>
        }
      </span>
    }
  `,
  styles: `
    :host {
      display: block;
    }
  `,
  imports: [LinkRouteComponent],
})
export class LocationNodeRoutesComponent {
  readonly node = input.required<LocationNodeInfo>();

  hasRouteReferences(): boolean {
    return this.node().routeReferences && this.node().routeReferences.length > 0;
  }
}
