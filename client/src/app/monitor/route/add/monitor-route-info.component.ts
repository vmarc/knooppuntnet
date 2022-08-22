import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteInfoPage } from '@api/common/monitor/monitor-route-info-page';

@Component({
  selector: 'kpn-monitor-route-info',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="section-title">Route information:</div>

    <div class="section-body">
      <kpn-data title="Ref">
        {{ routeInfo.ref }}
      </kpn-data>

      <kpn-data title="Name">
        {{ routeInfo.name }}
      </kpn-data>

      <kpn-data title="Operator">
        {{ routeInfo.operator }}
      </kpn-data>

      <kpn-data title="Relation">
        {{ routeInfo.wayCount }}
        <span>ways,</span>
        {{ routeInfo.nodeCount }}
        <span>nodes,</span>
        {{ routeInfo.relationCount }}
        <span>relations</span>
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
