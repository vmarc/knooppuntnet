import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { Country } from '@api/custom/country';
import { NetworkType } from '@api/custom/network-type';
import { Subset } from '@api/custom/subset';
import { Stat } from '../domain/stat';

@Component({
  selector: 'kpn-overview-list-stat-row',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <tr>
      <td *ngIf="rowspan" [rowSpan]="rowspan">
        <kpn-country-name [country]="country" />
      </td>
      <td>
        <kpn-network-type-icon [networkType]="networkType" />
      </td>
      <td class="value">
        <kpn-overview-value [stat]="stat" [subset]="subset(country, networkType)" />
      </td>
    </tr>
  `,
  styles: [
    `
      :host {
        display: contents;
      }

      .value {
        text-align: right;
        vertical-align: middle;
        width: 3.5em;
      }
    `,
  ],
})
export class OverviewListStatRowComponent {
  @Input() rowspan: number = null;
  @Input() country: Country;
  @Input() networkType: NetworkType;
  @Input() stat: Stat;

  subset(country: Country, networkType: NetworkType): Subset {
    return { country, networkType };
  }
}
