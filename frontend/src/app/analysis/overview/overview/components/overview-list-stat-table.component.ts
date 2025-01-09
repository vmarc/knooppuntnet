import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Stat } from '../../domain/stat';
import { OverviewListStatRowComponent } from './overview-list-stat-row.component';

@Component({
  selector: 'kpn-overview-list-stat-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <table class="kpn-table">
      <tbody>
        <kpn-overview-list-stat-row
          [stat]="stat()"
          country="nl"
          routeType="cycling"
          [rowspan]="6"
        />
        <kpn-overview-list-stat-row [stat]="stat()" country="nl" routeType="hiking" />
        <kpn-overview-list-stat-row [stat]="stat()" country="nl" routeType="horse-riding" />
        <kpn-overview-list-stat-row [stat]="stat()" country="nl" routeType="motorboat" />
        <kpn-overview-list-stat-row [stat]="stat()" country="nl" routeType="canoe" />
        <kpn-overview-list-stat-row [stat]="stat()" country="nl" routeType="inline-skating" />

        <kpn-overview-list-stat-row
          [stat]="stat()"
          country="be"
          routeType="cycling"
          [rowspan]="3"
        />
        <kpn-overview-list-stat-row [stat]="stat()" country="be" routeType="hiking" />
        <kpn-overview-list-stat-row [stat]="stat()" country="be" routeType="horse-riding" />

        <kpn-overview-list-stat-row
          [stat]="stat()"
          country="de"
          routeType="cycling"
          [rowspan]="3"
        />
        <kpn-overview-list-stat-row [stat]="stat()" country="de" routeType="hiking" />
        <kpn-overview-list-stat-row [stat]="stat()" country="de" routeType="horse-riding" />

        <kpn-overview-list-stat-row
          [stat]="stat()"
          country="fr"
          routeType="cycling"
          [rowspan]="4"
        />
        <kpn-overview-list-stat-row [stat]="stat()" country="fr" routeType="hiking" />
        <kpn-overview-list-stat-row [stat]="stat()" country="fr" routeType="horse-riding" />
        <kpn-overview-list-stat-row [stat]="stat()" country="fr" routeType="canoe" />

        <kpn-overview-list-stat-row
          [stat]="stat()"
          country="at"
          routeType="cycling"
          [rowspan]="1"
        />

        <kpn-overview-list-stat-row
          [stat]="stat()"
          country="es"
          routeType="cycling"
          [rowspan]="2"
        />

        <kpn-overview-list-stat-row [stat]="stat()" country="es" routeType="hiking" />

        <kpn-overview-list-stat-row
          [stat]="stat()"
          country="dk"
          routeType="cycling"
          [rowspan]="1"
        />

        <tr>
          <td colspan="2" i18n="@@overview.total">Total</td>
          <td class="value">
            {{ stat().total() }}
          </td>
        </tr>
      </tbody>
    </table>
  `,
  styles: `
    .value {
      text-align: right;
      vertical-align: middle;
      width: 3.5em;
    }
  `,
  imports: [OverviewListStatRowComponent],
})
export class OverviewListStatTableComponent {
  stat = input.required<Stat>();
}
