import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';

@Component({
  selector: 'kpn-monitor-admin-route-summary',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="section-title">
      Route information:
    </div>

    <div class="section-body">

      <kpn-data title="Name">
        <p>GR05 vlaanderen</p>
      </kpn-data>

      <kpn-data title="Operator">
        Operator
      </kpn-data>

      <kpn-data title="OSM">
        <p>
          master relation with 3 subrelations | single relation
        </p>
        <p>
          222km
        </p>
        <p>
          111 ways
        </p>
      </kpn-data>
    </div>
  `,
  styles: [`

    .section-title {
      padding-top: 2em;
    }

    .section-body {
      padding-left: 2em;
    }

  `]
})
export class MonitorAdminRouteSummaryComponent {
}
