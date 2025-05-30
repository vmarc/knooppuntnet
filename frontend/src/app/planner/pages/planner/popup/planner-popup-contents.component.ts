import { ChangeDetectionStrategy } from '@angular/core';
import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { PlannerPopupService } from '../../../domain/context/planner-popup-service';
import { PlannerPopupRouteComponent } from './planner-popup-route.component';
import { PlannerPopupNodeComponent } from './planner-popup-node.component';
import { PlannerPopupPoiComponent } from './planner-popup-poi.component';

@Component({
  selector: 'ui-planner-popup-contents',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.popupType(); as popupType) {
      @if (popupType === 'poi') {
        <ui-planner-popup-poi />
      }
      @if (popupType === 'node') {
        <ui-planner-popup-node />
      }
      @if (popupType === 'route') {
        <ui-planner-popup-route />
      }
    }
  `,
  imports: [PlannerPopupRouteComponent, PlannerPopupNodeComponent, PlannerPopupPoiComponent],
})
export class PlannerPopupContentsComponent {
  readonly service = inject(PlannerPopupService);
}
