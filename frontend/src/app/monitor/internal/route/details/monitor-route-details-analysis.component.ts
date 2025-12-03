import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MonitorRouteDetailsPage } from '@api/common/monitor/monitor-route-details-page';
import { DistancePipe } from '@app/shared/components/format/distance.pipe';
import { IconHappyComponent } from '@app/shared/components/icon/icon-happy.component';

@Component({
  selector: 'ui-monitor-route-details-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (page().happy) {
      <p class="kpn-line">
        <span i18n="@@monitor.route.details.analysis.ok">All ok</span>
        <ui-icon-happy />
      </p>
    } @else {
      <p>
        <span class="kpn-space-separated">
          <span>{{ page().summary.deviationCount }}</span>
          @if (page().summary.deviationCount === 1) {
            <span i18n="@@monitor.route.details.analysis.deviation"> deviation </span>
          } @else {
            <span i18n="@@monitor.route.details.analysis.deviations"> deviations </span>
          }
          @if (page().summary.deviationCount > 0) {
            <span class="kpn-brackets">
              <span>{{ page().deviationDistance | distance }}</span>
            </span>
          }
        </span>
      </p>

      <p>
        <span class="kpn-space-separated">
          <span>{{ page().summary.segmentCount }}</span>
          @if (page().summary.segmentCount === 1) {
            <span i18n="@@monitor.route.details.analysis.osm-segment"> OSM segment </span>
          } @else {
            <span i18n="@@monitor.route.details.analysis.osm-segments"> OSM segments </span>
          }
        </span>
      </p>
    }
  `,
  imports: [IconHappyComponent, DistancePipe, DistancePipe],
})
export class MonitorRouteDetailsAnalysisComponent {
  readonly page = input.required<MonitorRouteDetailsPage>();
}
