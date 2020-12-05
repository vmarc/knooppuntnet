import {ChangeDetectionStrategy, Component} from '@angular/core';

/* tslint:disable:template-i18n */
@Component({
  selector: 'kpn-long-distance-route-map-legend',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="legend">
      <p>Legend</p>
      <div class="kpn-line">
        <svg height="30" width="70">
          <line x1="0" y1="15" x2="60" y2="15" style="stroke:yellow;stroke-width:3"/>
        </svg>
        <span>OSM</span>
      </div>
      <div class="kpn-line">
        <svg height="30" width="70">
          <line x1="0" y1="15" x2="60" y2="15" style="stroke:blue;stroke-width:3"/>
        </svg>
        <span>GPX</span>
      </div>
    </div>
  `,
  styles: [`
    .legend {
      padding: 15px;
      border-bottom: 1px solid lightgray;
    }
  `]
})
export class LongDistanceRouteMapLegendComponent {
}
