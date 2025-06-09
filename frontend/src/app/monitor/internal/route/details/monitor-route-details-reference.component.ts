import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MonitorRouteDetailsPage } from '@api/common/monitor/monitor-route-details-page';
import { DistancePipe } from '@app/shared/components/format/distance.pipe';
import { TimestampPipe } from '@app/shared/components/format/timestamp-pipe';

@Component({
  selector: 'ui-monitor-route-details-reference',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (!page().referenceType) {
      <div i18n="@@monitor.route.details.reference-undefined">Reference not defined yet</div>
    }

    @if (page().referenceType === 'osm' || page().referenceType === 'gpx') {
      <p>{{ page().referenceTimestamp | yyyymmddhhmm }}</p>
    }

    @if (page().referenceType === 'osm') {
      <p i18n="@@monitor.route.details.reference.osm">OSM relation snapshot</p>
    }

    @if (page().referenceType === 'multi-gpx') {
      <p i18n="@@monitor.route.details.reference.multi-gpx">GPX trace per sub relation</p>
    }

    @if (page().referenceType === 'gpx') {
      <p>{{ 'GPX: "' + page().referenceFilename + '"' }}</p>
    }

    <p>{{ page().referenceDistance | distance }}</p>
  `,
  imports: [DistancePipe, TimestampPipe, TimestampPipe, DistancePipe],
})
export class MonitorRouteDetailsReferenceComponent {
  readonly page = input.required<MonitorRouteDetailsPage>();
}
