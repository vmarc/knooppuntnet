import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MapService } from '../../../../components/ol/services/map.service';

@Component({
  selector: 'kpn-map-popup-contents',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="mapService.popupType$ | async as popupType">
      <div [ngClass]="{ hidden: popupType !== 'poi' }">
        <kpn-map-popup-poi></kpn-map-popup-poi>
      </div>
      <div [ngClass]="{ hidden: popupType !== 'node' }">
        <kpn-map-popup-node></kpn-map-popup-node>
      </div>
      <div [ngClass]="{ hidden: popupType !== 'route' }">
        <kpn-map-popup-route></kpn-map-popup-route>
      </div>
    </div>
  `,
  styles: [
    `
      .hidden {
        display: none;
      }
    `,
  ],
})
export class MapPopupContentsComponent {
  constructor(public mapService: MapService) {}
}
