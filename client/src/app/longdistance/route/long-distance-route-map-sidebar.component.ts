import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';

@Component({
  selector: 'kpn-long-distance-route-map-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-long-distance-route-map-segments></kpn-long-distance-route-map-segments>
      <kpn-long-distance-route-map-legend></kpn-long-distance-route-map-legend>
    </kpn-sidebar>
  `
})
export class LongDistanceRouteMapSidebarComponent {
}
