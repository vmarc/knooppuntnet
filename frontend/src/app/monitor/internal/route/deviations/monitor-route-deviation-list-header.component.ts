import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteDeviationsPageService } from './monitor-route-deviations-page.service';
import { LegendLineComponent } from '@app/shared/components/legend-line';
import { NavService } from '@app/shared/components/nav.service';

@Component({
  selector: 'ui-monitor-route-deviation-list-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="deviation deviation-header">
      <span class="deviation-id">
        <ui-legend-line color="red" />
      </span>
      <span class="deviation-distance" i18n="@@monitor.route.deviations.deviation">
        Deviation
      </span>
      <span i18n="@@monitor.route.deviations.length">Length</span>
    </div>
  `,
  styleUrl: './monitor-route-deviation-list.scss',
  providers: [MonitorRouteDeviationsPageService, NavService],
  imports: [LegendLineComponent],
})
export class MonitorRouteDeviationListHeaderComponent {}
