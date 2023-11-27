import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteDetailsPage } from '@api/common/monitor';
import { TimestampPipe } from '@app/components/shared/format';
import { TimestampDayPipe } from '@app/components/shared/format';
import { DistancePipe } from '@app/components/shared/format';
import { DayPipe } from '@app/components/shared/format';

@Component({
  selector: 'kpn-monitor-route-details-reference',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (!page.referenceType) {
      <div i18n="@@monitor.route.details.reference-undefined">Reference not defined yet</div>
    }

    @if (page.referenceType === 'osm' || page.referenceType === 'gpx') {
      <p>{{ page.referenceTimestamp | yyyymmddhhmm }}</p>
    }

    @if (page.referenceType === 'osm') {
      <p i18n="@@monitor.route.details.reference.osm">OSM relation snapshot</p>
    }

    @if (page.referenceType === 'multi-gpx') {
      <p i18n="@@monitor.route.details.reference.multi-gpx">GPX trace per sub relation</p>
    }

    @if (page.referenceType === 'gpx') {
      <p>{{ 'GPX: "' + page.referenceFilename + '"' }}</p>
    }

    <p>{{ page.referenceDistance | distance }}</p>
  `,
  standalone: true,
  imports: [DayPipe, DistancePipe, TimestampDayPipe, TimestampPipe],
})
export class MonitorRouteDetailsReferenceComponent {
  @Input({ required: true }) page: MonitorRouteDetailsPage;
}
