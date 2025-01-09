import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Country } from '@api/common';
import { RouteType } from '@api/common';
import { Subset } from '@api/custom';
import { CountryNameComponent } from '@app/components/shared';
import { RouteTypeIconComponent } from '@app/components/shared';
import { Stat } from '../../domain/stat';
import { OverviewValueComponent } from './overview-value.component';

@Component({
  selector: 'kpn-overview-list-stat-row',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <tr>
      @if (rowspan()) {
        <td [rowSpan]="rowspan()">
          <kpn-country-name [country]="country()" />
        </td>
      }
      <td>
        <kpn-route-type-icon [routeType]="routeType()" />
      </td>
      <td class="value">
        <kpn-overview-value [stat]="stat()" [subset]="subset(country(), routeType())" />
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
  imports: [CountryNameComponent, RouteTypeIconComponent, OverviewValueComponent],
})
export class OverviewListStatRowComponent {
  rowspan = input<number>(null);
  country = input.required<Country>();
  routeType = input.required<RouteType>();
  stat = input.required<Stat>();

  subset(country: Country, routeType: RouteType): Subset {
    return { country, routeType };
  }
}
