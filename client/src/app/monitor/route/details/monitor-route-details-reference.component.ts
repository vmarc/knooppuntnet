import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteDetailsPage } from '@api/common/monitor/monitor-route-details-page';

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
    <p *ngIf="!!page.referenceType">
      <span>{{ page.referenceDay | day }}</span>
    </p>
    <p
      *ngIf="page.referenceType === 'osm'"
      i18n="@@monitor.route.details.reference.osm"
    >
      OSM relation snapshot
    </p>
    <div *ngIf="page.referenceType === 'gpx'">
      <p>
        {{ 'GPX: "' + page.referenceFilename + '"' }}
      </p>
    </div>
    <p class="kpn-km">{{ page.referenceDistance }}</p>
  `,
})
export class MonitorRouteDetailsReferenceComponent {
  @Input() page: MonitorRouteDetailsPage;
}
