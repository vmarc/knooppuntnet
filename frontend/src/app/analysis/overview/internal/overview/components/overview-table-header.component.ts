import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Subsets } from '@app/shared/kpn/common/subsets';
import { NzIconDirective } from 'ng-zorro-antd/icon';

@Component({
  selector: 'ui-overview-table-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <tr>
      <th rowspan="2" i18n="@@overview-table.detail">Detail</th>
      <th rowspan="2" i18n="@@overview-table.total">Total</th>
      <th colspan="6" i18n="@@country.nl">The Netherlands</th>
      <th colspan="3" i18n="@@country.be">Belgium</th>
      <th colspan="2" i18n="@@country.de">Germany</th>
      <th colspan="4" i18n="@@country.fr">France</th>
      <th colspan="1" i18n="@@country.at">Austria</th>
      <th colspan="2" i18n="@@country.es">Spain</th>
      <th colspan="1" i18n="@@country.dk">Denmark</th>
      <th colspan="2" i18n="@@country.pl">Poland</th>
      <th rowspan="2" i18n="@@overview-table.comment">Comment</th>
    </tr>
    <tr>
      @for (subset of subsets(); track subset) {
        <th class="value-cell">
          <nz-icon [nzType]="subset.routeType" />
        </th>
      }
    </tr>
  `,
  styles: `
    :host {
      display: table-header-group;
    }
  `,
  imports: [NzIconDirective],
})
export class OverviewTableHeaderComponent {
  subsets() {
    return Subsets.all;
  }
}
