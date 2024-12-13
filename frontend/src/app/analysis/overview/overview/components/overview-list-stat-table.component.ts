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
          networkType="cycling"
          [rowspan]="6"
        />
        <kpn-overview-list-stat-row [stat]="stat()" country="nl" networkType="hiking" />
        <kpn-overview-list-stat-row [stat]="stat()" country="nl" networkType="horse-riding" />
        <kpn-overview-list-stat-row [stat]="stat()" country="nl" networkType="motorboat" />
        <kpn-overview-list-stat-row [stat]="stat()" country="nl" networkType="canoe" />
        <kpn-overview-list-stat-row [stat]="stat()" country="nl" networkType="inline-skating" />

        <kpn-overview-list-stat-row
          [stat]="stat()"
          country="be"
          networkType="cycling"
          [rowspan]="3"
        />
        <kpn-overview-list-stat-row [stat]="stat()" country="be" networkType="hiking" />
        <kpn-overview-list-stat-row [stat]="stat()" country="be" networkType="horse-riding" />

        <kpn-overview-list-stat-row
          [stat]="stat()"
          country="de"
          networkType="cycling"
          [rowspan]="3"
        />
        <kpn-overview-list-stat-row [stat]="stat()" country="de" networkType="hiking" />
        <kpn-overview-list-stat-row [stat]="stat()" country="de" networkType="horse-riding" />

        <kpn-overview-list-stat-row
          [stat]="stat()"
          country="fr"
          networkType="cycling"
          [rowspan]="4"
        />
        <kpn-overview-list-stat-row [stat]="stat()" country="fr" networkType="hiking" />
        <kpn-overview-list-stat-row [stat]="stat()" country="fr" networkType="horse-riding" />
        <kpn-overview-list-stat-row [stat]="stat()" country="fr" networkType="canoe" />

        <kpn-overview-list-stat-row
          [stat]="stat()"
          country="at"
          networkType="cycling"
          [rowspan]="1"
        />

        <kpn-overview-list-stat-row
          [stat]="stat()"
          country="es"
          networkType="cycling"
          [rowspan]="2"
        />

        <kpn-overview-list-stat-row [stat]="stat()" country="es" networkType="hiking" />

        <kpn-overview-list-stat-row
          [stat]="stat()"
          country="dk"
          networkType="cycling"
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
