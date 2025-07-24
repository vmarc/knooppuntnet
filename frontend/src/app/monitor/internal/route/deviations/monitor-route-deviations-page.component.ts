import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteDeviationInfo } from '@api/common/monitor/monitor-route-deviation-info';
import { MonitorRouteDeviationListComponent } from '@app/monitor/internal/route/deviations/monitor-route-deviation-list.component';
import { MonitorRouteDeviationsPageService } from '@app/monitor/internal/route/deviations/monitor-route-deviations-page.service';
import { NavService } from '@app/shared/components/nav.service';

@Component({
  selector: 'ui-monitor-route-deviations-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (response.hasValue()) {
      <ui-monitor-route-deviation-list
        [deviations]="deviations()"
        (selectionChange)="selectDeviation($event)"
      />
    }
  `,
  providers: [MonitorRouteDeviationsPageService, NavService],
  imports: [MonitorRouteDeviationListComponent],
})
export class MonitorRouteDeviationsPageComponent {
  readonly service = inject(MonitorRouteDeviationsPageService);
  readonly response = this.service.response;
  protected readonly deviations = computed(() => this.response.value()?.result?.deviations);

  selectDeviation(deviation: MonitorRouteDeviationInfo): void {
    this.service.selectDeviation(deviation);
  }
}
