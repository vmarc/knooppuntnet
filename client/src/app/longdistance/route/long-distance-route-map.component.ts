import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';

@Component({
  selector: 'kpn-long-distance-route-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-long-distance-route-page-header pageName="map"></kpn-long-distance-route-page-header>
  `,
  styles: [`
  `]
})
export class LongDistanceRouteMapComponent {
}
