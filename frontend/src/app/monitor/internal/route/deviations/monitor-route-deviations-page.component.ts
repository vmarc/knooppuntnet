import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteDeviationInfo } from '@api/common/monitor/monitor-route-deviation-info';
import { MonitorRouteDeviationListComponent } from '@app/monitor/internal/route/deviations/monitor-route-deviation-list.component';
import { MonitorRouteDeviationsPageService } from '@app/monitor/internal/route/deviations/monitor-route-deviations-page.service';
import { NavService } from '@app/shared/components/nav.service';
import { PageComponent } from '@app/shared/components/page/page.component';
import { MonitorRoutePageHeaderComponent } from '../components/monitor-route-page-header.component';

@Component({
  selector: 'ui-monitor-route-deviations-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-monitor-route-page-header pageName="deviations" />
      @if (service.response(); as response) {
        @if (!response.result) {
          <div class="kpn-error" i18n="@@monitor.route.details.not-found">Route not found</div>
        }
        @if (response.result; as page) {
          <ui-monitor-route-deviation-list
            [deviations]="deviations()"
            (selectionChange)="selectDeviation($event)"
          />
        }
      }
    </ui-page>
  `,
  providers: [MonitorRouteDeviationsPageService, NavService],
  imports: [MonitorRoutePageHeaderComponent, PageComponent, MonitorRouteDeviationListComponent],
})
export class MonitorRouteDeviationsPageComponent {
  readonly service = inject(MonitorRouteDeviationsPageService);
  protected readonly deviations = computed(() => this.service.response()?.result?.deviations);

  selectDeviation(deviation: MonitorRouteDeviationInfo): void {
    this.service.selectDeviation(deviation);
  }
}
