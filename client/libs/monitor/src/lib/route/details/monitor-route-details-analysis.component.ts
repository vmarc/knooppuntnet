import { NgIf } from '@angular/common';
import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteDetailsPage } from '@api/common/monitor';
import { DistancePipe } from '@app/components/shared/format';
import { IconHappyComponent } from '@app/components/shared/icon';

@Component({
  selector: 'kpn-monitor-route-details-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p *ngIf="page.happy" class="kpn-line">
      <span i18n="@@monitor.route.details.analysis.ok">All ok</span>
      <kpn-icon-happy />
    </p>
    <div *ngIf="!page.happy">
      <p>
        <span>{{ page.deviationCount }}</span>
        <span
          *ngIf="page.deviationCount === 1"
          i18n="@@monitor.route.details.analysis.deviation"
        >
          deviation
        </span>
        <span
          *ngIf="page.deviationCount !== 1"
          i18n="@@monitor.route.details.analysis.deviations"
        >
          deviations
        </span>
        <span *ngIf="page.deviationCount > 0" class="kpn-brackets">
          <span>{{ page.deviationDistance | distance }}</span>
        </span>
      </p>
      <p>
        <span>{{ page.osmSegmentCount }}</span>
        <span
          *ngIf="page.osmSegmentCount === 1"
          i18n="@@monitor.route.details.analysis.osm-segment"
        >
          OSM segment
        </span>
        <span
          *ngIf="page.osmSegmentCount !== 1"
          i18n="@@monitor.route.details.analysis.osm-segments"
        >
          OSM segments
        </span>
      </p>
    </div>
  `,
  standalone: true,
  imports: [NgIf, IconHappyComponent, DistancePipe],
})
export class MonitorRouteDetailsAnalysisComponent {
  @Input({ required: true }) page: MonitorRouteDetailsPage;
}
