import { NgIf } from '@angular/common';
import { NgClass } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MapService } from '../../../services/map.service';
import { MapPopupRouteComponent } from './map-popup-route.component';
import { PlannerPopupNodeComponent } from './planner-popup-node.component';
import { PlannerPopupPoiComponent } from './planner-popup-poi.component';

@Component({
  selector: 'kpn-planner-popup-contents',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="mapService.popupType$ | async as popupType">
      <div [ngClass]="{ hidden: popupType !== 'poi' }">
        <kpn-planner-popup-poi />
      </div>
      <div [ngClass]="{ hidden: popupType !== 'node' }">
        <kpn-planner-popup-node />
      </div>
      <div [ngClass]="{ hidden: popupType !== 'route' }">
        <kpn-planner-popup-route />
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
  standalone: true,
  imports: [
    AsyncPipe,
    MapPopupRouteComponent,
    NgClass,
    NgIf,
    PlannerPopupNodeComponent,
    PlannerPopupPoiComponent,
  ],
})
export class PlannerPopupContentsComponent {
  constructor(public mapService: MapService) {}
}
