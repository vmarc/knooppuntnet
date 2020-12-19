import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';

@Component({
  selector: 'kpn-longdistance-route-map-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-longdistance-route-map-control></kpn-longdistance-route-map-control>
    </kpn-sidebar>
  `
})
export class LongdistanceRouteMapSidebarComponent {
}
