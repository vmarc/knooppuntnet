import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';

@Component({
  selector: 'kpn-long-distance-route-map-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-long-distance-route-map-control></kpn-long-distance-route-map-control>
    </kpn-sidebar>
  `
})
export class LongDistanceRouteMapSidebarComponent {
}
