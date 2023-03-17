import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MapService } from '@app/components/ol/services/map.service';

@Component({
  selector: 'kpn-planner-popup-contents',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="mapService.popupType$ | async as popupType">
      <div [ngClass]="{ hidden: popupType !== 'poi' }">
        <kpn-planner-popup-poi/>
      </div>
      <div [ngClass]="{ hidden: popupType !== 'node' }">
        <kpn-planner-popup-node/>
      </div>
      <div [ngClass]="{ hidden: popupType !== 'route' }">
        <kpn-planner-popup-route/>
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
export class PlannerPopupContentsComponent {
  constructor(public mapService: MapService) {}
}
