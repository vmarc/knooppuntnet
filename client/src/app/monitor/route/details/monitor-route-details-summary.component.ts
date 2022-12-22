import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteDetailsPage } from '@api/common/monitor/monitor-route-details-page';

@Component({
  selector: 'kpn-monitor-route-details-summary',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p
      *ngIf="!page.relationId"
      i18n="@@monitor.route.details.relation-id-undefined"
    >
      Route relation has not been defined yet
    </p>
    <div *ngIf="page.relationId">
      <p class="kpn-separated">
        <kpn-osm-link-relation
          [title]="page.relationId.toString()"
          [relationId]="page.relationId"
        ></kpn-osm-link-relation>
        <kpn-josm-relation [relationId]="page.relationId"></kpn-josm-relation>
      </p>
      <p class="kpn-space-separated">
        <span>{{ page.wayCount }}</span>
        <span i18n="@@monitor.route.details.ways">ways</span>
      </p>
      <p>{{ page.osmDistance | distance }}</p>
    </div>
  `,
})
export class MonitorRouteDetailsSummaryComponent {
  @Input() page: MonitorRouteDetailsPage;
}
