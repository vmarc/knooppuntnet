import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Subsets } from '../../../kpn/common/subsets';

@Component({
  selector: 'kpn-overview-table-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <tr>
      <th rowspan="2" i18n="@@overview-table.detail">Detail</th>
      <th rowspan="2" i18n="@@overview-table.total">Total</th>
      <th colspan="6" i18n="@@country.nl">The Netherlands</th>
      <th colspan="3" i18n="@@country.be">Belgium</th>
      <th colspan="2" i18n="@@country.de">Germany</th>
      <th colspan="2" i18n="@@country.fr">France</th>
      <th colspan="1" i18n="@@country.at">Austria</th>
      <th colspan="2" i18n="@@country.es">Spain</th>
      <th rowspan="2" i18n="@@overview-table.comment">Comment</th>
    </tr>
    <tr>
      <th class="value-cell" *ngFor="let subset of subsets()">
        <mat-icon [svgIcon]="subset.networkType"/>
      </th>
    </tr>
  `,
  styles: [
    `
      :host {
        display: table-header-group;
      }
    `,
  ],
})
export class OverviewTableHeaderComponent {
  subsets() {
    return Subsets.all;
  }
}
