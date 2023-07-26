import { NgIf } from '@angular/common';
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
    <div
      *ngIf="!page.referenceType"
      i18n="@@monitor.route.details.reference-undefined"
    >
      Reference not defined yet
    </div>
    <p *ngIf="page.referenceType === 'osm' || page.referenceType === 'gpx'">
      <span>{{ page.referenceTimestamp | yyyymmddhhmm }}</span>
    </p>
    <p
      *ngIf="page.referenceType === 'osm'"
      i18n="@@monitor.route.details.reference.osm"
    >
      OSM relation snapshot
    </p>
    <p
      *ngIf="page.referenceType === 'multi-gpx'"
      i18n="@@monitor.route.details.reference.multi-gpx"
    >
      GPX trace per sub relation
    </p>
    <div *ngIf="page.referenceType === 'gpx'">
      <p>
        {{ 'GPX: "' + page.referenceFilename + '"' }}
      </p>
    </div>
    <p>{{ page.referenceDistance | distance }}</p>
  `,
  standalone: true,
  imports: [NgIf, DayPipe, DistancePipe, TimestampDayPipe, TimestampPipe],
})
export class MonitorRouteDetailsReferenceComponent {
  @Input({ required: true }) page: MonitorRouteDetailsPage;
}
