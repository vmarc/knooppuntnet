import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteInfoPage } from '@api/common/monitor/monitor-route-info-page';

@Component({
  selector: 'kpn-monitor-route-info',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="section-title" i18n="@@monitor.relation.title">
      Route information:
    </div>

    <div class="section-body">
      <kpn-data title="Relation" i18n-title="@@monitor.relation.summary.label">
        {{ routeInfo.wayCount }}
        <span i18n="@@monitor.relation.summary.ways">ways,</span>
        {{ routeInfo.nodeCount }}
        <span i18n="@@monitor.relation.summary.nodes">nodes,</span>
        {{ routeInfo.relationCount }}
        <span i18n="@@monitor.relation.summary.relations">relations</span>
      </kpn-data>

      <kpn-data
        *ngIf="routeInfo.ref"
        title="Ref"
        i18n-title="@@monitor.relation.ref"
      >
        {{ routeInfo.ref }}
      </kpn-data>

      <kpn-data
        *ngIf="routeInfo.name"
        title="Name"
        i18n-title="@@monitor.relation.name"
      >
        {{ routeInfo.name }}
      </kpn-data>

      <kpn-data
        *ngIf="routeInfo.operator"
        title="Operator"
        i18n-title="@@monitor.relation.operator"
      >
        {{ routeInfo.operator }}
      </kpn-data>
    </div>
  `,
  styles: [
    `
      .section-title {
        padding-top: 2em;
      }

      .section-body {
        padding-top: 1em;
        padding-left: 2em;
      }
    `,
  ],
})
export class MonitorRouteInfoComponent {
  @Input() routeInfo: MonitorRouteInfoPage;
}
