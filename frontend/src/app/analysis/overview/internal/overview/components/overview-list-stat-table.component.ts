import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Stat } from '../../domain/stat';
import { OverviewListStatRowComponent } from './overview-list-stat-row.component';

@Component({
  selector: 'ui-overview-list-stat-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <table class="kpn-table">
      <tbody>
        <ui-overview-list-stat-row [stat]="stat()" country="nl" routeType="cycling" [rowspan]="6" />
        <ui-overview-list-stat-row [stat]="stat()" country="nl" routeType="hiking" />
        <ui-overview-list-stat-row [stat]="stat()" country="nl" routeType="horse-riding" />
        <ui-overview-list-stat-row [stat]="stat()" country="nl" routeType="motorboat" />
        <ui-overview-list-stat-row [stat]="stat()" country="nl" routeType="canoe" />
        <ui-overview-list-stat-row [stat]="stat()" country="nl" routeType="inline-skating" />

        <ui-overview-list-stat-row [stat]="stat()" country="be" routeType="cycling" [rowspan]="3" />
        <ui-overview-list-stat-row [stat]="stat()" country="be" routeType="hiking" />
        <ui-overview-list-stat-row [stat]="stat()" country="be" routeType="horse-riding" />

        <ui-overview-list-stat-row [stat]="stat()" country="de" routeType="cycling" [rowspan]="3" />
        <ui-overview-list-stat-row [stat]="stat()" country="de" routeType="hiking" />
        <ui-overview-list-stat-row [stat]="stat()" country="de" routeType="horse-riding" />

        <ui-overview-list-stat-row [stat]="stat()" country="fr" routeType="cycling" [rowspan]="4" />
        <ui-overview-list-stat-row [stat]="stat()" country="fr" routeType="hiking" />
        <ui-overview-list-stat-row [stat]="stat()" country="fr" routeType="horse-riding" />
        <ui-overview-list-stat-row [stat]="stat()" country="fr" routeType="canoe" />

        <ui-overview-list-stat-row [stat]="stat()" country="at" routeType="cycling" [rowspan]="1" />

        <ui-overview-list-stat-row [stat]="stat()" country="es" routeType="cycling" [rowspan]="2" />

        <ui-overview-list-stat-row [stat]="stat()" country="es" routeType="hiking" />

        <ui-overview-list-stat-row [stat]="stat()" country="dk" routeType="cycling" [rowspan]="1" />

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
  readonly stat = input.required<Stat>();
}
