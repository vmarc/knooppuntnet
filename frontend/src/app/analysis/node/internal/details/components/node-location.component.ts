import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LocationInfo } from '@api/common/location-info';
import { RouteType } from '@api/common/route-type';

@Component({
  selector: 'kpn-node-location',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (!hasLocation()) {
      <p i18n="@@node.location.none">None</p>
    }
    <div class="kpn-comma-list">
      @for (locationInfo of locations(); track locationInfo.name; let i = $index) {
        <a [routerLink]="link(locationInfo)">{{ locationInfo.name }}</a>
      }
    </div>
  `,
  imports: [RouterLink],
})
export class NodeLocationComponent {
  routeType = input.required<RouteType>();
  locations = input.required<LocationInfo[]>();

  hasLocation() {
    return this.locations() && this.locations().length > 0;
  }

  link(locationInfo: LocationInfo): string {
    return `/analysis/${this.routeType()}/${locationInfo.link}/details`;
  }
}
