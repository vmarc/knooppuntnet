import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { Country } from '@api/custom/country';
import { NetworkType } from '@api/custom/network-type';
import { Stat } from '../domain/stat';

@Component({
  selector: 'kpn-overview-list-stat-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <table class="kpn-table">
      <tbody>
        <kpn-overview-list-stat-row
          [stat]="stat"
          [country]="country.nl"
          [networkType]="networkType.cycling"
          [rowspan]="6"
        ></kpn-overview-list-stat-row>
        <kpn-overview-list-stat-row
          [stat]="stat"
          [country]="country.nl"
          [networkType]="networkType.hiking"
        ></kpn-overview-list-stat-row>
        <kpn-overview-list-stat-row
          [stat]="stat"
          [country]="country.nl"
          [networkType]="networkType.horseRiding"
        ></kpn-overview-list-stat-row>
        <kpn-overview-list-stat-row
          [stat]="stat"
          [country]="country.nl"
          [networkType]="networkType.motorboat"
        ></kpn-overview-list-stat-row>
        <kpn-overview-list-stat-row
          [stat]="stat"
          [country]="country.nl"
          [networkType]="networkType.canoe"
        ></kpn-overview-list-stat-row>
        <kpn-overview-list-stat-row
          [stat]="stat"
          [country]="country.nl"
          [networkType]="networkType.inlineSkating"
        ></kpn-overview-list-stat-row>

        <kpn-overview-list-stat-row
          [stat]="stat"
          [country]="country.be"
          [networkType]="networkType.cycling"
          [rowspan]="3"
        ></kpn-overview-list-stat-row>
        <kpn-overview-list-stat-row
          [stat]="stat"
          [country]="country.be"
          [networkType]="networkType.hiking"
        ></kpn-overview-list-stat-row>
        <kpn-overview-list-stat-row
          [stat]="stat"
          [country]="country.be"
          [networkType]="networkType.horseRiding"
        ></kpn-overview-list-stat-row>

        <kpn-overview-list-stat-row
          [stat]="stat"
          [country]="country.de"
          [networkType]="networkType.cycling"
          [rowspan]="3"
        ></kpn-overview-list-stat-row>
        <kpn-overview-list-stat-row
          [stat]="stat"
          [country]="country.de"
          [networkType]="networkType.hiking"
        ></kpn-overview-list-stat-row>
        <kpn-overview-list-stat-row
          [stat]="stat"
          [country]="country.de"
          [networkType]="networkType.horseRiding"
        ></kpn-overview-list-stat-row>

        <kpn-overview-list-stat-row
          [stat]="stat"
          [country]="country.fr"
          [networkType]="networkType.cycling"
          [rowspan]="4"
        ></kpn-overview-list-stat-row>
        <kpn-overview-list-stat-row
          [stat]="stat"
          [country]="country.fr"
          [networkType]="networkType.hiking"
        ></kpn-overview-list-stat-row>
        <kpn-overview-list-stat-row
          [stat]="stat"
          [country]="country.fr"
          [networkType]="networkType.horseRiding"
        ></kpn-overview-list-stat-row>
        <kpn-overview-list-stat-row
          [stat]="stat"
          [country]="country.fr"
          [networkType]="networkType.canoe"
        ></kpn-overview-list-stat-row>

        <kpn-overview-list-stat-row
          [stat]="stat"
          [country]="country.at"
          [networkType]="networkType.cycling"
          [rowspan]="1"
        ></kpn-overview-list-stat-row>

        <kpn-overview-list-stat-row
          [stat]="stat"
          [country]="country.es"
          [networkType]="networkType.cycling"
          [rowspan]="2"
        ></kpn-overview-list-stat-row>

        <kpn-overview-list-stat-row
          [stat]="stat"
          [country]="country.es"
          [networkType]="networkType.hiking"
        ></kpn-overview-list-stat-row>

        <tr>
          <td colspan="2" i18n="@@overview.total">Total</td>
          <td class="value">
            {{ stat.total() }}
          </td>
        </tr>
      </tbody>
    </table>
  `,
  styles: [
    `
      .value {
        text-align: right;
        vertical-align: middle;
        width: 3.5em;
      }
    `,
  ],
})
export class OverviewListStatTableComponent {
  @Input() stat: Stat;

  country = Country;
  networkType = NetworkType;
}
