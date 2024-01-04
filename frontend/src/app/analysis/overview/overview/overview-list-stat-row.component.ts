import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { Country } from '@api/custom';
import { NetworkType } from '@api/custom';
import { Subset } from '@api/custom';
import { CountryNameComponent } from '@app/components/shared';
import { NetworkTypeIconComponent } from '@app/components/shared';
import { Stat } from '../domain/stat';
import { OverviewValueComponent } from './overview-value.component';

@Component({
  selector: 'kpn-overview-list-stat-row',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <tr>
      @if (rowspan) {
        <td [rowSpan]="rowspan">
          <kpn-country-name [country]="country" />
        </td>
      }
      <td>
        <kpn-network-type-icon [networkType]="networkType" />
      </td>
      <td class="value">
        <kpn-overview-value [stat]="stat" [subset]="subset(country, networkType)" />
      </td>
    </tr>
  `,
  styles: `
    :host {
      display: contents;
    }

    .value {
      text-align: right;
      vertical-align: middle;
      width: 3.5em;
    }
  `,
  standalone: true,
  imports: [CountryNameComponent, NetworkTypeIconComponent, OverviewValueComponent],
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
