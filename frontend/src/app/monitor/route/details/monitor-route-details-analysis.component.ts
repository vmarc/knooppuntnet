import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MonitorRouteDetailsPage } from '@api/common/monitor';
import { DistancePipe } from '@app/components/shared/format';
import { IconHappyComponent } from '@app/components/shared/icon';

@Component({
  selector: 'kpn-monitor-route-details-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (page().happy) {
      <p class="kpn-line">
        <span i18n="@@monitor.route.details.analysis.ok">All ok</span>
        <kpn-icon-happy />
      </p>
    } @else {
      <p>
        <span class="kpn-space-separated">
          <span>{{ page().deviationCount }}</span>
          @if (page().deviationCount === 1) {
            <span i18n="@@monitor.route.details.analysis.deviation"> deviation </span>
          }
          @if (page().deviationCount !== 1) {
            <span i18n="@@monitor.route.details.analysis.deviations"> deviations </span>
          }
          @if (page().deviationCount > 0) {
            <span class="kpn-brackets">
              <span>{{ page().deviationDistance | distance }}</span>
            </span>
          }
        </span>
      </p>

      <p>
        <span class="kpn-space-separated">
          <span>{{ page().osmSegmentCount }}</span>
          @if (page().osmSegmentCount === 1) {
            <span i18n="@@monitor.route.details.analysis.osm-segment"> OSM segment </span>
          }
          @if (page().osmSegmentCount !== 1) {
            <span i18n="@@monitor.route.details.analysis.osm-segments"> OSM segments </span>
          }
        </span>
      </p>
    }
  `,
  standalone: true,
  imports: [IconHappyComponent, DistancePipe],
})
export class MonitorRouteDetailsAnalysisComponent {
  page = input.required<MonitorRouteDetailsPage>();
}
