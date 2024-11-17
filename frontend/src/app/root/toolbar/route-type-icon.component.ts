import { computed } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { NetworkType } from '@api/custom';

@Component({
  selector: 'kpn-route-type-icon',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: '<mat-icon>{{ icon() }}</mat-icon>',
  standalone: true,
  imports: [MatIconModule],
})
export class RouteTypeIconItemComponent {
  readonly networkType = input.required<NetworkType>();
  readonly icon = computed(() => {
    switch (this.networkType()) {
      case NetworkType.cycling:
        return 'directions_bike';
      case NetworkType.hiking:
        return 'directions_walk';
      case NetworkType.horseRiding:
        return 'bedroom_baby';
      case NetworkType.motorboat:
        return 'directions_boat';
      case NetworkType.canoe:
        return 'kayaking';
      case NetworkType.inlineSkating:
        return 'roller_skating';
      default:
        return '';
    }
  });
}
