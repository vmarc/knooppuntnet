import { NgClass } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { PlannerPopupService } from '../../../domain/context/planner-popup-service';
import { PlannerPopupRouteComponent } from './planner-popup-route.component';
import { PlannerPopupNodeComponent } from './planner-popup-node.component';
import { PlannerPopupPoiComponent } from './planner-popup-poi.component';

@Component({
  selector: 'kpn-planner-popup-contents',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.popupType(); as popupType) {
      @if (popupType === 'poi') {
        <kpn-planner-popup-poi />
      }
      @if (popupType === 'node') {
        <kpn-planner-popup-node />
      }
      @if (popupType === 'route') {
        <kpn-planner-popup-route />
      }
    }
  `,
  standalone: true,
  imports: [
    PlannerPopupRouteComponent,
    NgClass,
    PlannerPopupNodeComponent,
    PlannerPopupPoiComponent,
  ],
})
export class PlannerPopupContentsComponent {
  protected readonly service = inject(PlannerPopupService);
}
