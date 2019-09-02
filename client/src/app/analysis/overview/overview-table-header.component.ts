import {Component} from "@angular/core";
import {Subset} from "../../kpn/shared/subset";

@Component({
  selector: "kpn-overview-table-header",
  template: `
    <tr>
      <th rowspan="2" i18n="@@overview-table.detail">Detail</th>
      <th rowspan="2" i18n="@@overview-table.total">Total</th>
      <th colspan="6" i18n="@@country.nl">The Netherlands</th>
      <th colspan="3" i18n="@@country.be">Belgium</th>
      <th colspan="3" i18n="@@country.de">Germany</th>
      <th rowspan="2" i18n="@@overview-table.comment">Comment</th>
    </tr>
    <tr>
      <th class="value-cell" *ngFor="let subset of subsets()">
        <mat-icon [svgIcon]="subset.networkType.name"></mat-icon>
      </th>
    </tr>
  `,
  styles: [`
    :host {
      display: table-header-group;
    }
  `]
})
export class OverviewTableHeaderComponent {
  subsets() {
    return Subset.all;
  }
}
