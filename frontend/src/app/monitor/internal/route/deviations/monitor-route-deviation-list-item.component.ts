import { output } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteDeviationInfo } from '@api/common/monitor/monitor-route-deviation-info';
import { DeviationPopupEvent } from '@app/monitor/internal/route/deviations/deviation-popup-event';
import { DistancePipe } from '@app/shared/components/format/distance.pipe';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzIconDirective } from 'ng-zorro-antd/icon';

@Component({
  selector: 'ui-monitor-route-deviation-list-item',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let dev = deviation();
    <div (contextmenu)="popupMenu($event, dev)" class="deviation">
      <span class="deviation-id">{{ dev.id }}</span>

      @if (dev.distance === 2500) {
        <span class="kpn-nowrap deviation-distance">{{ longDistance }}</span>
      } @else {
        <span class="kpn-nowrap deviation-distance">{{ dev.distance | distance }}</span>
      }

      <span class="kpn-nowrap">{{ dev.meters | distance }}</span>

      <button
        nz-button
        nzShape="circle"
        (click)="popupMenu($event, dev)"
        class="deviation-popup-button"
      >
        <nz-icon nzType="ellipsis" />
      </button>
    </div>
  `,
  styleUrl: './monitor-route-deviation-list.scss',
  imports: [DistancePipe, NzButtonComponent, NzIconDirective],
})
export class MonitorRouteDeviationListItemComponent {
  readonly deviation = input.required<MonitorRouteDeviationInfo>();
  readonly popup = output<DeviationPopupEvent>();

  readonly longDistance = '> 2.5 km';

  popupMenu(event: MouseEvent, deviation: MonitorRouteDeviationInfo): void {
    event.stopPropagation();
    this.popup.emit({ event, deviation });
  }
}
