import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LocationInfo } from '@api/common/location-info';
import { NetworkType } from '@api/custom';

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
  standalone: true,
  imports: [RouterLink],
})
export class NodeLocationComponent {
  networkType = input.required<NetworkType>();
  locations = input.required<LocationInfo[]>();

  hasLocation() {
    return this.locations() && this.locations().length > 0;
  }

  link(locationInfo: LocationInfo): string {
    return `/analysis/${this.networkType()}/${locationInfo.link}/details`;
  }
}
