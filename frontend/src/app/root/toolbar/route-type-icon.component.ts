import { computed } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouteType } from '@api/common';

@Component({
  selector: 'kpn-route-type-icon',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: '<mat-icon>{{ icon() }}</mat-icon>',
  imports: [MatIconModule],
})
export class RouteTypeIconItemComponent {
  readonly routeType = input.required<RouteType>();
  readonly icon = computed(() => {
    switch (this.routeType()) {
      case 'cycling':
        return 'directions_bike';
      case 'hiking':
        return 'directions_walk';
      case 'horse-riding':
        return 'bedroom_baby';
      case 'motorboat':
        return 'directions_boat';
      case 'canoe':
        return 'kayaking';
      case 'inline-skating':
        return 'roller_skating';
      default:
        return '';
    }
  });
}
